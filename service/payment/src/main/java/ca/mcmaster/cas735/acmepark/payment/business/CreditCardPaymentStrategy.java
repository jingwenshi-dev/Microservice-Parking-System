package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.ports.PaymentStrategy;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Component
@Slf4j
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean pay(BigDecimal amount) {
        log.info("Processing credit card payment of {}", amount);
        // 模拟支付逻辑，90% 成功率
        return Math.random() > 0.1;
    }
}
