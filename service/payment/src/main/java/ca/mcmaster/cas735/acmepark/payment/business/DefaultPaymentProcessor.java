package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentProcessor;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentSender;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentServicePort;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.TicketDeleteSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefaultPaymentProcessor implements PaymentProcessor {

    private final PaymentServicePort paymentService;
    private final PaymentSender paymentSender;
    private final TicketDeleteSender ticketDeleteSender;

    @Autowired
    public DefaultPaymentProcessor(PaymentServicePort paymentService,
                                   PaymentSender paymentSender,
                                   TicketDeleteSender ticketDeleteSender) {
        this.paymentService = paymentService;
        this.paymentSender = paymentSender;
        this.ticketDeleteSender = ticketDeleteSender;
    }

    @Override
    public void processPayment(PaymentRequest paymentRequest) {
        // 进行扣费获取支付结果
        boolean result = paymentService.processPayment(paymentRequest);
        paymentRequest.setResult(result);

        // 根据 userType 决定支付结果发送的目标服务
        switch (paymentRequest.getUserType().toLowerCase()) {
            case "visitor":
                log.info("Sending payment result to Visitor service for license plate: {}", paymentRequest.getLicensePlate());
                paymentSender.sendPaymentResultToVisitor(paymentRequest);
                break;

            case "student":
            case "staff":
                log.info("Sending payment result to Permit service for license plate: {}", paymentRequest.getLicensePlate());
                paymentSender.sendPaymentResultToPermit(paymentRequest);
                break;

            default:
                throw new IllegalArgumentException("Unknown user type: " + paymentRequest.getUserType());
        }

        ticketDeleteSender.sendTicketDelete(paymentRequest);

    }
}