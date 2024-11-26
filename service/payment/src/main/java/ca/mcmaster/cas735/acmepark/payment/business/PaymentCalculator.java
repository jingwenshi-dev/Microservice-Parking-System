package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.ports.PaymentCalculatorPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PaymentCalculator implements PaymentCalculatorPort {

    @Override
    public BigDecimal calculateParkingFee(LocalDateTime entryTime, LocalDateTime exitTime, BigDecimal hourlyRate) {
        Duration duration = Duration.between(entryTime, exitTime);
        long hours = duration.toHours();
        if (duration.toMinutes() % 60 > 0) {
            hours += 1; // 按整小时计算，即使多出几分钟
        }
        return hourlyRate.multiply(BigDecimal.valueOf(hours));
    }
}