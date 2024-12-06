package ca.mcmaster.cas735.acmepark.visitor_access;

import ca.mcmaster.cas735.acmepark.gate.dto.ValidationDTO;
import ca.mcmaster.cas735.acmepark.visitor_access.business.GateInteractionServiceImpl;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.EntryRequestHandler;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.ExitRequestHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GateInteractionServiceImplTests {
    @Mock
    private EntryRequestHandler entryRequestHandler;
    @Mock
    private ExitRequestHandler exitRequestHandler;

    private GateInteractionServiceImpl gateInteractionService;

    @BeforeEach
    void setUp() {
        gateInteractionService = new GateInteractionServiceImpl(entryRequestHandler, exitRequestHandler);
    }

    @Test
    void handleGateRequest_EntryRequest_CallsEntryHandler() {
        // Arrange
        String jsonData = "{"
                + "\"transponderId\":\"transponder1\","
                + "\"licensePlate\":\"ABC123\","
                + "\"gateId\":\"Gate1\","
                + "\"entry\":true,"
                + "\"timestamp\":\"" + LocalDateTime.now() + "\","
                + "\"lotId\":1,"
                + "\"visitorAllowed\":true,"
                + "\"hourlyRate\":10.0"
                + "}";

        // Act
        gateInteractionService.handleGateRequest(jsonData);

        // Assert
        verify(entryRequestHandler, times(1)).handleEntry(any(ValidationDTO.class));
        verify(exitRequestHandler, never()).handleExit(any());
    }

    @Test
    void handleGateRequest_ExitRequest_CallsExitHandler() {
        // Arrange
        String jsonData = "{"
                + "\"transponderId\":\"transponder1\","
                + "\"licensePlate\":\"ABC123\","
                + "\"gateId\":\"Gate1\","
                + "\"entry\":false,"
                + "\"timestamp\":\"" + LocalDateTime.now() + "\","
                + "\"lotId\":1,"
                + "\"visitorAllowed\":true,"
                + "\"hourlyRate\":10.0"
                + "}";
        // Act
        gateInteractionService.handleGateRequest(jsonData);

        // Assert
        verify(exitRequestHandler, times(1)).handleExit(any(ValidationDTO.class));
        verify(entryRequestHandler, never()).handleEntry(any());
    }
}
