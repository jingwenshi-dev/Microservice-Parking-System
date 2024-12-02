package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.payment.ports.ViolationInquirySender;
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
import java.util.List;

/**
 * 扣费程序入口，用processPayment进行扣费
 */
@Service
@Slf4j
public class PaymentService implements PaymentServicePort {

    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentCalculatorFactory paymentCalculatorFactory;
    private final ViolationInquirySender violationInquirySender;

    @Autowired
    public PaymentService(PaymentStrategyFactory paymentStrategyFactory, PaymentCalculatorFactory paymentCalculatorFactory, ViolationInquirySender violationInquirySender) {
        this.paymentStrategyFactory = paymentStrategyFactory;
        this.paymentCalculatorFactory = paymentCalculatorFactory;
        this.violationInquirySender = violationInquirySender;
    }

    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        try {
            // 根据用户类型选择支付计算器
            PaymentCalculatorPort paymentCalculator = paymentCalculatorFactory.getPaymentCalculator(paymentRequest.getUserType());

            // 计算停车费用
            BigDecimal amount = paymentCalculator.calculateParkingFee(paymentRequest);

            // Send request for violation inquiry and calculate total fines
            List<TicketDTO> violations = violationInquirySender.sendInquiry(paymentRequest.getLicensePlate());
            BigDecimal totalFines = BigDecimal.ZERO;

            if (!violations.isEmpty()) {
                for (TicketDTO violation : violations) {
                    totalFines = totalFines.add(violation.getFineAmount());
                }
            }

            // 计算总费用
            BigDecimal totalAmount = amount.add(totalFines);
            paymentRequest.setAmount(totalAmount);

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