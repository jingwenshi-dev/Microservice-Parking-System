package ca.mcmaster.cas735.acmepark.payment;

import ca.mcmaster.cas735.acmepark.payment.business.PaymentExecutorImpl;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.factory.PaymentStrategyFactory;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentStrategy;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.TotalFeeCalculator;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.VoucherManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentExecutorImplTests {

    @Mock
    private PaymentStrategyFactory paymentStrategyFactory;

    @Mock
    private TotalFeeCalculator totalFeeCalculator;

    @Mock
    private VoucherManager voucherManager;

    @Mock
    private PaymentStrategy paymentStrategy;

    private PaymentExecutorImpl paymentExecutor;

    @BeforeEach
    void setUp() {
        paymentExecutor = new PaymentExecutorImpl(paymentStrategyFactory, totalFeeCalculator, voucherManager);
    }

    /**
     * Verifies that a valid voucher is detected and no payment is required
     */
    @Test
    void testExecutePayment_ValidVoucher() {
        PaymentRequest request = new PaymentRequest();
        request.setLicensePlate("ABC123");

        when(voucherManager.hasValidActiveVoucher("ABC123")).thenReturn(true);

        boolean result = paymentExecutor.executePayment(request);

        assertTrue(result);
        verify(voucherManager, times(1)).hasValidActiveVoucher("ABC123");
        verifyNoInteractions(totalFeeCalculator);
        verifyNoInteractions(paymentStrategyFactory);
    }

    /**
     * Verifies that a payment is processed successfully
     */
    @Test
    void testExecutePayment_Success() {
        PaymentRequest request = new PaymentRequest();
        request.setLicensePlate("ABC123");
        request.setPaymentMethod("creditCard");

        when(voucherManager.hasValidActiveVoucher("ABC123")).thenReturn(false);
        when(totalFeeCalculator.calculateTotalFee(request)).thenReturn(new BigDecimal("50.00"));
        when(paymentStrategyFactory.getPaymentStrategy("creditCard")).thenReturn(paymentStrategy);
        when(paymentStrategy.pay(any())).thenReturn(true);

        boolean result = paymentExecutor.executePayment(request);

        assertTrue(result);
        verify(totalFeeCalculator, times(1)).calculateTotalFee(request);
        verify(paymentStrategy, times(1)).pay(new BigDecimal("50.00"));
    }

    /**
     * Verifies that a payment fails
     */
    @Test
    void testExecutePayment_Failed() {
        PaymentRequest request = new PaymentRequest();
        request.setLicensePlate("ABC123");
        request.setPaymentMethod("creditCard");

        when(voucherManager.hasValidActiveVoucher("ABC123")).thenReturn(false);
        when(totalFeeCalculator.calculateTotalFee(request)).thenReturn(new BigDecimal("50.00"));
        when(paymentStrategyFactory.getPaymentStrategy("creditCard")).thenReturn(paymentStrategy);
        when(paymentStrategy.pay(any())).thenReturn(false);

        boolean result = paymentExecutor.executePayment(request);

        assertFalse(result);
    }
}