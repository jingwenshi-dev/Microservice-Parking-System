package ca.mcmaster.cas735.acmepark.gate;

import ca.mcmaster.cas735.acmepark.gate.business.GateReqHandler;
import ca.mcmaster.cas735.acmepark.gate.dto.TransponderDTO;
import ca.mcmaster.cas735.acmepark.gate.port.ValidationReqSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GateReqHandlerTests {

    @Mock
    private ValidationReqSender validationReqSender;

    private GateReqHandler gateReqHandler;

    @BeforeEach
    void setUp() {
        gateReqHandler = new GateReqHandler(validationReqSender);
    }

    /**
     * Verifies when a transponder is read, `GateReqHandler` triggers the `readTransponder` method on the `gateReqHandler`.
     * Verifies `readTransponder` method calls the `send` method of the `validationReqSender` mock object.
     * Verifies that `send` is called exactly once with the transponder as an argument.
     */
    @Test
    void testSendsValidationRequest() {
        TransponderDTO transponder = new TransponderDTO();
        transponder.setTransponderId("");
        transponder.setLicensePlate("ABC123");
        transponder.setGateId("G1");
        transponder.setLotId(1L);
        transponder.setEntry(true);

        gateReqHandler.readTransponder(transponder);

        verify(validationReqSender, times(1)).send(transponder);
    }
}