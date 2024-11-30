package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.business.entities.ParkingViolation;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentCalculatorPort;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.factory.PaymentCalculatorFactory;
import ca.mcmaster.cas735.acmepark.payment.factory.PaymentStrategyFactory;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentStrategy;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentServicePort;
import ca.mcmaster.cas735.acmepark.payment.ports.required.ParkingViolationsRepository;
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
    private final ParkingViolationsRepository violationsRepository;

    @Autowired
    public PaymentService(PaymentStrategyFactory paymentStrategyFactory,
                          PaymentCalculatorFactory paymentCalculatorFactory,
                          ParkingViolationsRepository violationsRepository) {
        this.paymentStrategyFactory = paymentStrategyFactory;
        this.paymentCalculatorFactory = paymentCalculatorFactory;
        this.violationsRepository = violationsRepository;
    }

    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        try {
            // TODO:确认是否需要port and adapter
            // 根据用户类型选择支付计算器
            PaymentCalculatorPort paymentCalculator = paymentCalculatorFactory.getPaymentCalculator(paymentRequest.getUserType());

            // 计算停车费用
            BigDecimal amount = paymentCalculator.calculateParkingFee(paymentRequest);

            // 查询未支付的罚单金额
            List<ParkingViolation> unpaidViolations = violationsRepository.findByLicensePlateAndIsPaidFalse(paymentRequest.getLicensePlate());
            BigDecimal totalFines = unpaidViolations.stream()
                    .map(ParkingViolation::getFineAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 计算总费用
            BigDecimal totalAmount = paymentRequest.getAmount().add(totalFines);
            paymentRequest.setAmount(totalAmount);
            // TODO：支付成功，删除之前的罚款
            // TODO: 测试需要单独创建一个吗，还是直接在项目里面，老师怎么验证所有的逻辑

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