package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.ports.provided.*;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.factory.PaymentStrategyFactory;
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
    private final TotalFeeCalculator totalFeeCalculator;
    private final VoucherManager manager;

    @Autowired
    public PaymentService(PaymentStrategyFactory paymentStrategyFactory,
                          TotalFeeCalculator totalFeeCalculator,
                          VoucherManager manager) {
        this.paymentStrategyFactory = paymentStrategyFactory;
        this.totalFeeCalculator = totalFeeCalculator;
        this.manager = manager;
    }

    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        try {
            // There are vouchers, direct payment success
            if (manager.hasValidActiveVoucher(paymentRequest.getLicensePlate())) {
                return true;
            }
            // Calculate price
            BigDecimal amount = totalFeeCalculator.calculateTotalFee(paymentRequest);

            // Choose a payment strategy based on the payment method
            PaymentStrategy paymentStrategy = paymentStrategyFactory.getPaymentStrategy(paymentRequest.getPaymentMethod());

            // Implementation payments
            boolean paymentSuccess = paymentStrategy.pay(amount);

            if (paymentSuccess) {
                log.info("Payment of {} for license plate {} has been successfully processed.",
                        amount, paymentRequest.getLicensePlate());
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