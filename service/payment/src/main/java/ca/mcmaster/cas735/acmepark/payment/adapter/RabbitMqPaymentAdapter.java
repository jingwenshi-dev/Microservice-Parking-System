package ca.mcmaster.cas735.acmepark.payment.adapter;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequestDto;
import ca.mcmaster.cas735.acmepark.payment.ports.PaymentCalculatorPort;
import ca.mcmaster.cas735.acmepark.payment.ports.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class RabbitMqPaymentAdapter implements PaymentProcessor {

    private final PaymentCalculatorPort paymentCalculator;

    @Autowired
    public RabbitMqPaymentAdapter(PaymentCalculatorPort paymentCalculator) {
        this.paymentCalculator = paymentCalculator;
    }

    public boolean processPayment(PaymentRequestDto paymentRequest) {
        log.info("Received payment request for license plate: {}", paymentRequest.getLicensePlate());
        BigDecimal hourlyRate = new BigDecimal("5.0"); // 示例停车费率

        try {
            // 计算费用
            BigDecimal amount = paymentCalculator.calculateParkingFee(paymentRequest.getEntryTime(), paymentRequest.getExitTime(), hourlyRate);
            paymentRequest.setAmount(amount);

            // 执行扣费逻辑，例如向用户的账户扣除金额
            boolean paymentSuccess = deductAmount(paymentRequest);

            // 模拟返回支付成功结果
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

    // 模拟的扣费方法，返回扣款是否成功
    private boolean deductAmount(PaymentRequestDto paymentRequest) {
        // TODO: 执行实际的扣费逻辑，这里只是模拟
        // 假设扣款操作有一定概率失败
        return Math.random() > 0.1; // 90% 成功，10% 失败
    }
}