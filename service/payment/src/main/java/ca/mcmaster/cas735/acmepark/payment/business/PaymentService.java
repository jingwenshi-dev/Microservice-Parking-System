package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentCalculatorPort;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.factory.PaymentCalculatorFactory;
import ca.mcmaster.cas735.acmepark.payment.factory.PaymentStrategyFactory;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentStrategy;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 扣费程序入口，用processPayment进行扣费
 */
@Service
@Slf4j
public class PaymentService implements PaymentServicePort {

    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentCalculatorFactory paymentCalculatorFactory;

    @Autowired
    public PaymentService(PaymentStrategyFactory paymentStrategyFactory, PaymentCalculatorFactory paymentCalculatorFactory) {
        this.paymentStrategyFactory = paymentStrategyFactory;
        this.paymentCalculatorFactory = paymentCalculatorFactory;
    }

    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        try {
            // 根据用户类型选择支付计算器
            PaymentCalculatorPort paymentCalculator = paymentCalculatorFactory.getPaymentCalculator(paymentRequest.getUserType());

            // 计算费用
            BigDecimal amount = paymentCalculator.calculateParkingFee(paymentRequest);

            // 设置计算后的费用
            paymentRequest.setAmount(amount);

            // 根据支付方式选择支付策略
            PaymentStrategy paymentStrategy = paymentStrategyFactory.getPaymentStrategy(paymentRequest.getPaymentMethod());

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