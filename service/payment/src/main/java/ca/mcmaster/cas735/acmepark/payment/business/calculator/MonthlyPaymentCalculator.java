package ca.mcmaster.cas735.acmepark.payment.business.calculator;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentCalculatorPort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class MonthlyPaymentCalculator implements PaymentCalculatorPort {

    @Override
    public BigDecimal calculateParkingFee(PaymentRequest paymentRequest) {
        // Calculated on a monthly basis, ignoring specific entry and exit times
        return new BigDecimal(30);
    }
}