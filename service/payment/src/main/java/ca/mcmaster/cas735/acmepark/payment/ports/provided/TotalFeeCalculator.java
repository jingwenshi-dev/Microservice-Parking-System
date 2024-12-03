package ca.mcmaster.cas735.acmepark.payment.ports.provided;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;

import java.math.BigDecimal;

public interface TotalFeeCalculator {
    BigDecimal calculateTotalFee(PaymentRequest paymentRequest);
}
