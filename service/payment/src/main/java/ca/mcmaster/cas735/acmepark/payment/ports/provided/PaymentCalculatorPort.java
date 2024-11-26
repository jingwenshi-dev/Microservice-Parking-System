package ca.mcmaster.cas735.acmepark.payment.ports.provided;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;

import java.math.BigDecimal;

public interface PaymentCalculatorPort {
    BigDecimal calculateParkingFee(PaymentRequest paymentRequest);
}
