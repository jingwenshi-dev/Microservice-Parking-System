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
public class PaymentExecutorImpl implements PaymentExecutor {

    private final PaymentStrategyFactory paymentStrategyFactory;
    private final TotalFeeCalculator totalFeeCalculator;
    private final VoucherManager voucherManager;

    @Autowired
    public PaymentExecutorImpl(PaymentStrategyFactory paymentStrategyFactory,
                               TotalFeeCalculator totalFeeCalculator,
                               VoucherManager voucherManager) {
        this.paymentStrategyFactory = paymentStrategyFactory;
        this.totalFeeCalculator = totalFeeCalculator;
        this.voucherManager = voucherManager;
    }

    @Override
    public boolean executePayment(PaymentRequest paymentRequest) {
        try {
            // Check for valid voucher
            if (voucherManager.hasValidActiveVoucher(paymentRequest.getLicensePlate())) {
                return true;
            }

            // Calculate total amount
            BigDecimal amount = totalFeeCalculator.calculateTotalFee(paymentRequest);

            // Get and execute payment strategy
            PaymentStrategy paymentStrategy =
                    paymentStrategyFactory.getPaymentStrategy(paymentRequest.getPaymentMethod());
            boolean paymentSuccess = paymentStrategy.pay(amount);

            if (paymentSuccess) {
                log.info("Payment of {} for license plate {} has been successfully processed.",
                        amount, paymentRequest.getLicensePlate());
            } else {
                log.warn("Payment of {} for license plate {} failed to process.",
                        amount, paymentRequest.getLicensePlate());
            }
            return paymentSuccess;

        } catch (Exception e) {
            log.error("Error processing payment for license plate {}: {}",
                    paymentRequest.getLicensePlate(), e.getMessage());
            return false;
        }
    }
}