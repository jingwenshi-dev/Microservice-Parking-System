package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequestDto;
import ca.mcmaster.cas735.acmepark.payment.factory.PaymentStrategyFactory;
import ca.mcmaster.cas735.acmepark.payment.ports.PaymentStrategy;
import ca.mcmaster.cas735.acmepark.payment.ports.PaymentServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class PaymentService implements PaymentServicePort {

    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentCalculator paymentCalculator;

    @Autowired
    public PaymentService(PaymentStrategyFactory paymentStrategyFactory, PaymentCalculator paymentCalculator) {
        this.paymentStrategyFactory = paymentStrategyFactory;
        this.paymentCalculator = paymentCalculator;
    }

    @Override
    public boolean processPayment(PaymentRequestDto paymentRequest) {
        try {
            // 根据支付方式选择支付策略
            PaymentStrategy paymentStrategy = paymentStrategyFactory.getPaymentStrategy(paymentRequest.getPaymentMethod());

            // 计算费用
            BigDecimal amount = paymentCalculator.calculateParkingFee(paymentRequest.getEntryTime(), paymentRequest.getExitTime(), paymentRequest.getHourlyRate());

            // 设置计算后的费用
            paymentRequest.setAmount(amount);

            // 执行支付
            boolean paymentSuccess = paymentStrategy.pay(amount);

            if (paymentSuccess) {
                log.info("Payment of {} for license plate {} has been successfully processed.", amount, paymentRequest.getLicensePlate());
            } else {
                log.warn("Payment of {} for license plate {} failed to process.", amount, paymentRequest.getLicensePlate());
            }
            return paymentSuccess;

        } catch (Exception e) {
            log.error("Error processing payment for license plate {}: {}", paymentRequest.getLicensePlate(), e.getMessage());
            return false;
        }
    }
}