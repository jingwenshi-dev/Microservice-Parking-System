package ca.mcmaster.cas735.acmepark.payment.business.calculator;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.payment.factory.PaymentCalculatorFactory;
import ca.mcmaster.cas735.acmepark.payment.ports.ViolationInquirySender;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentCalculatorPort;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.TotalFeeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TotalFeeCalculatorImpl implements TotalFeeCalculator {

    private final PaymentCalculatorFactory paymentCalculatorFactory;
    private final ViolationInquirySender violationInquirySender;

    @Autowired
    public TotalFeeCalculatorImpl(PaymentCalculatorFactory paymentCalculatorFactory,
                                  ViolationInquirySender violationInquirySender) {
        this.paymentCalculatorFactory = paymentCalculatorFactory;
        this.violationInquirySender = violationInquirySender;
    }

    @Override
    public BigDecimal calculateTotalFee(PaymentRequest paymentRequest) {
        // Choose a payment calculator based on user type
        PaymentCalculatorPort paymentCalculator =
                paymentCalculatorFactory.getPaymentCalculator(paymentRequest.getUserType());

        // Calculating Parking Fees
        BigDecimal amount = paymentCalculator.calculateParkingFee(paymentRequest);

        // Send request for violation inquiry and calculate total fines
        List<TicketDTO> violations = violationInquirySender.sendInquiry(paymentRequest.getLicensePlate());
        BigDecimal totalFines = BigDecimal.ZERO;

        if (!violations.isEmpty()) {
            for (TicketDTO violation : violations) {
                totalFines = totalFines.add(violation.getFineAmount());
            }
        }

        // Calculate the total cost
        return amount.add(totalFines);
    }
}
