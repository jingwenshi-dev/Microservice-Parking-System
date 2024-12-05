package ca.mcmaster.cas735.acmepark.visitor_access;

import ca.mcmaster.cas735.acmepark.visitor_access.business.PaymentInteractionImpl;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.VisitorSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentInteractionImplTests {
    @Mock
    private VisitorSender visitorSender;

    private PaymentInteractionImpl paymentInteraction;

    @BeforeEach
    void setUp() {
        paymentInteraction = new PaymentInteractionImpl(visitorSender);
    }

    @Test
    void testHandlePaymentResult_SuccessfulPayment() {
        // Arrange
        String jsonData = "{"
                + "\"userType\":\"visitor\","
                + "\"licensePlate\":\"ABC123\","
                + "\"lotId\":1,"
                + "\"userId\":12345,"
                + "\"permitType\":\"temporary\","
                + "\"transponderNumber\":\"550e8400-e29b-41d4-a716-446655440000\","
                + "\"paymentMethod\":\"creditCard\","
                + "\"payrollNum\":\"\","
                + "\"amount\":20.50,"
                + "\"entryTime\":\"2024-12-01T08:00:00\","
                + "\"hourlyRate\":2.50,"
                + "\"result\":true,"
                + "\"gateId\":\"Gate1\""
                + "}";

        // Act
        paymentInteraction.handlePaymentResult(jsonData);

        // Assert
        verify(visitorSender).sendExitResponseToGate(argThat(gateCtrl ->
                gateCtrl.getGateId().equals("Gate1") &&
                        gateCtrl.getLotId().equals(1L) &&
                        gateCtrl.getIsValid() &&
                        !gateCtrl.getIsEntry()
        ));
    }

    @Test
    void testHandlePaymentResult_FailedPayment() {
        // Arrange
        String jsonData = "{"
                + "\"userType\":\"visitor\","
                + "\"licensePlate\":\"ABC123\","
                + "\"lotId\":1,"
                + "\"userId\":12345,"
                + "\"permitType\":\"temporary\","
                + "\"transponderNumber\":\"550e8400-e29b-41d4-a716-446655440000\","
                + "\"paymentMethod\":\"creditCard\","
                + "\"payrollNum\":\"\","
                + "\"amount\":20.50,"
                + "\"entryTime\":\"2024-12-01T08:00:00\","
                + "\"hourlyRate\":2.50,"
                + "\"result\":false,"
                + "\"gateId\":\"Gate1\""
                + "}";
        // Act
        paymentInteraction.handlePaymentResult(jsonData);

        // Assert
        verify(visitorSender).sendExitResponseToGate(argThat(gateCtrl ->
                gateCtrl.getGateId().equals("Gate1") &&
                        gateCtrl.getLotId().equals(1L) &&
                        !gateCtrl.getIsValid() &&
                        !gateCtrl.getIsEntry()
        ));
    }
}
