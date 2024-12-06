package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentHandler;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentSender;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentExecutor;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.TicketDeleteSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentOrchestrator implements PaymentHandler {

    private final PaymentExecutor paymentExecutor;
    private final PaymentSender paymentSender;
    private final TicketDeleteSender ticketDeleteSender;

    @Autowired
    public PaymentOrchestrator(PaymentExecutor paymentExecutor,
                               PaymentSender paymentSender,
                               TicketDeleteSender ticketDeleteSender) {
        this.paymentExecutor = paymentExecutor;
        this.paymentSender = paymentSender;
        this.ticketDeleteSender = ticketDeleteSender;
    }

    @Override
    public void handlePayment(PaymentRequest paymentRequest) {
        // Execute payment and get result
        boolean result = paymentExecutor.executePayment(paymentRequest);
        paymentRequest.setResult(result);

        // Route payment result based on user type
        switch (paymentRequest.getUserType().toLowerCase()) {
            case "visitor":
                log.info("Sending payment result to Visitor service for license plate: {}", paymentRequest.getLicensePlate());
                paymentSender.sendPaymentResultToVisitor(paymentRequest);
                break;

            case "student":
            case "staff":
                log.info("Sending payment result to Permit service for license plate: {}",
                        paymentRequest.getLicensePlate());
                paymentSender.sendPaymentResultToPermit(paymentRequest);
                break;

            default:
                throw new IllegalArgumentException("Unknown user type: " + paymentRequest.getUserType());
        }

        ticketDeleteSender.sendTicketDelete(paymentRequest);
    }
}