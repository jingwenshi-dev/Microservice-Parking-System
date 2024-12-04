package ca.mcmaster.cas735.acmepark.payment.business.paymentStrategy;

import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentStrategy;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Component
@Slf4j
public class PayrollDeductionPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean pay(BigDecimal amount) {
        log.info("Processing payroll deduction payment of {}", amount);
        // Analog payment logic, 100% success rate
        return Math.random() > 0;
    }
}
