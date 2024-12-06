package ca.mcmaster.cas735.acmepark.payment;

import ca.mcmaster.cas735.acmepark.payment.business.PaymentOrchestrator;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentExecutor;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentSender;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.TicketDeleteSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentOrchestratorTests {
    @Mock
    private PaymentExecutor paymentExecutor;

    @Mock
    private PaymentSender paymentSender;

    @Mock
    private TicketDeleteSender ticketDeleteSender;

    private PaymentOrchestrator paymentOrchestrator;

    @BeforeEach
    void setUp() {
        paymentOrchestrator = new PaymentOrchestrator(paymentExecutor, paymentSender, ticketDeleteSender);
    }

    @Test
    void testHandlePayment_Visitor() {
        PaymentRequest request = new PaymentRequest();
        request.setUserType("visitor");
        request.setLicensePlate("ABC123");

        when(paymentExecutor.executePayment(request)).thenReturn(true);

        paymentOrchestrator.handlePayment(request);

        verify(paymentSender, times(1)).sendPaymentResultToVisitor(request);
        verify(ticketDeleteSender, times(1)).sendTicketDelete(request);
        assertTrue(request.isResult());
    }

    @Test
    void testHandlePayment_PermitHolder() {
        PaymentRequest request = new PaymentRequest();
        request.setUserType("student");
        request.setLicensePlate("ABC123");

        when(paymentExecutor.executePayment(request)).thenReturn(true);

        paymentOrchestrator.handlePayment(request);

        verify(paymentSender, times(1)).sendPaymentResultToPermit(request);
        verify(ticketDeleteSender, times(1)).sendTicketDelete(request);
        assertTrue(request.isResult());
    }

    @Test
    void handlePayment_InvalidUserType_ThrowsException() {
        PaymentRequest request = new PaymentRequest();
        request.setUserType("RandomUserType");

        assertThrows(IllegalArgumentException.class, () -> paymentOrchestrator.handlePayment(request));

        verifyNoInteractions(paymentSender);
        verifyNoInteractions(ticketDeleteSender);
    }
}