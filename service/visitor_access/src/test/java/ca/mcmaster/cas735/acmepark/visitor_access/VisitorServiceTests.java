package ca.mcmaster.cas735.acmepark.visitor_access;


import ca.mcmaster.cas735.acmepark.visitor_access.adapter.VisitorServiceImpl;
import ca.mcmaster.cas735.acmepark.visitor_access.domain.PaymentRequest;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.GateClient;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.VisitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class VisitorServiceTests {

    @Mock
    private GateClient gateClient;

    @InjectMocks
    private VisitorServiceImpl visitorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleVisitorEntry_Success() {
        String qrCode = "validQrCode";

        visitorService.handleVisitorEntry(qrCode);

        verify(gateClient, times(1)).sendOpenGateRequest("open_gate:visitor");
    }

    @Test
    public void testHandleVisitorExit_Success() {
        String qrCode = "validQrCode";
        visitorService.processVisitorPayment(qrCode, true);

        visitorService.handleVisitorExit(qrCode);

        verify(gateClient, times(1)).sendOpenGateRequest("open_gate:visitor");
    }

    @Test
    public void testHandleVisitorExit_Fail_NoPayment() {
        String qrCode = "validQrCode";

        visitorService.handleVisitorExit(qrCode);

        verify(gateClient, never()).sendOpenGateRequest(anyString());
    }

    @Test
    public void testProcessVisitorPayment() {
        String qrCode = "validQrCode";
        boolean paymentStatus = true;

        visitorService.processVisitorPayment(qrCode, paymentStatus);

        // Assuming there's a method to verify payment status, this test ensures payment is processed correctly
    }

    @Test
    public void testTranslate_Success() {
        String raw = "\"validQrCode\"";
        String result = visitorService.translate(raw);

        assertEquals("validQrCode", result);
    }

    @Test
    public void testTranslatePayment_Success() {
        String raw = "{\"qrCode\":\"validQrCode\",\"paymentStatus\":true}";
        PaymentRequest paymentRequest = visitorService.translatePayment(raw);

        assertEquals("validQrCode", paymentRequest.getQrCode());
        assertTrue(paymentRequest.isPaymentStatus());
    }
}

