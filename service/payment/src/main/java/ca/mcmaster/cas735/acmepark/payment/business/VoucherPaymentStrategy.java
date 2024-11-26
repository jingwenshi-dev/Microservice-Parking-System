package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.ports.PaymentStrategy;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Component
@Slf4j
public class VoucherPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean pay(BigDecimal amount) {
        log.info("Processing voucher payment of {}", amount);
        // 模拟支付逻辑，100% 成功率，因通行证已经预付
        return true;
    }
}
