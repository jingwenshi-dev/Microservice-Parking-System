package ca.mcmaster.cas735.acmepark.payment.ports;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PaymentCalculatorPort {
    BigDecimal calculateParkingFee(LocalDateTime entryTime, LocalDateTime exitTime, BigDecimal hourlyRate);
}
