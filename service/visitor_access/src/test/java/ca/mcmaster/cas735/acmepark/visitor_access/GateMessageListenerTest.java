package ca.mcmaster.cas735.acmepark.visitor_access;

import ca.mcmaster.cas735.acmepark.visitor_access.adapter.GateMessageListener;
import ca.mcmaster.cas735.acmepark.visitor_access.adapter.VisitorMessageListener;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.GateInteractionHandler;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.VisitorRequestHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

public class GateMessageListenerTest {

    @Mock
    private GateInteractionHandler gateInteractionHandler;

    @Mock
    private VisitorRequestHandler visitorRequestHandler;

    @Mock
    private Logger log;

    @InjectMocks
    private GateMessageListener gateMessageListener;

    @InjectMocks
    private VisitorMessageListener visitorMessageListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListenForGateEntryResponse() {
        String data = "{\"licensePlate\":\"ABC123\",\"entry\":true}";

        // Act
        gateMessageListener.listenForGateEntryResponse(data);

        // Assert
        verify(gateInteractionHandler, times(1)).handleGateEntryResponse(data);
    }

    @Test
    public void testListenForGateExitResponse() {
        String data = "{\"licensePlate\":\"XYZ789\",\"entry\":false}";

        // Act
        gateMessageListener.listenForGateExitResponse(data);

        // Assert
        verify(gateInteractionHandler, times(1)).handleGateExitResponse(data);
    }

    @Test
    public void testListenForVisitorEntry() {
        String data = "{\"licensePlate\":\"ABC123\",\"entry\":true}";

        // Act
        visitorMessageListener.listenForVisitorEntry(data);

        // Assert
        verify(visitorRequestHandler, times(1)).handleVisitorEntry(data);
    }

    @Test
    public void testListenForVisitorExit() {
        String data = "{\"licensePlate\":\"XYZ789\",\"entry\":false}";

        // Act
        visitorMessageListener.listenForVisitorExit(data);

        // Assert
        verify(visitorRequestHandler, times(1)).handleVisitorExit(data);
    }
}
