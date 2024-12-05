package ca.mcmaster.cas735.acmepark.visitor_access;

import ca.mcmaster.cas735.acmepark.gate.dto.ValidationDTO;
import ca.mcmaster.cas735.acmepark.visitor_access.business.EntryReqHandlerImpl;
import ca.mcmaster.cas735.acmepark.visitor_access.business.entities.Visitor;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.QRCodeService;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.VisitorSender;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.required.VisitorDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntryReqHandlerImplTests {
    @Mock
    private VisitorSender visitorSender;
    @Mock
    private QRCodeService qrCodeService;
    @Mock
    private VisitorDataRepository visitorDataRepository;

    private EntryReqHandlerImpl entryRequestHandler;

    @BeforeEach
    void setUp() {
        entryRequestHandler = new EntryReqHandlerImpl(visitorSender, qrCodeService, visitorDataRepository);
    }

    /**
     * Verifies that the handleEntry method a gate open response when the visitor is allowed to enter.
     */
    @Test
    void testHandleEntry_Success() throws Exception {
        // Arrange
        ValidationDTO validationDTO = new ValidationDTO();
        validationDTO.setTransponderId("transponder1");
        validationDTO.setLicensePlate("ABC123");
        validationDTO.setGateId("Gate1");
        validationDTO.setEntry(true);
        validationDTO.setTimestamp(LocalDateTime.now());
        validationDTO.setLotId(1L);
        validationDTO.setVisitorAllowed(true);
        validationDTO.setHourlyRate(BigDecimal.valueOf(10.0));

        String mockQRCode = "mocked-qr-code";
        when(qrCodeService.generateQRCode(anyString())).thenReturn(mockQRCode);

        // Act
        entryRequestHandler.handleEntry(validationDTO);

        // Assert
        verify(visitorDataRepository, times(1)).save(any(Visitor.class));
        verify(visitorSender).sendEntryResponseToGate(argThat(gateCtrl ->
            gateCtrl.getGateId().equals("Gate1") &&
            gateCtrl.getLotId().equals(1L) &&
            gateCtrl.getIsValid() &&
            gateCtrl.getIsEntry() &&
            gateCtrl.getQrCode().equals(mockQRCode)
        ));
    }

    @Test
    void testHandleEntry_DeniesAccess() {
        // Arrange
        ValidationDTO validationDTO = new ValidationDTO();
        validationDTO.setTransponderId("transponder1");
        validationDTO.setLicensePlate("ABC123");
        validationDTO.setGateId("Gate1");
        validationDTO.setEntry(true);
        validationDTO.setTimestamp(LocalDateTime.now());
        validationDTO.setLotId(1L);
        validationDTO.setVisitorAllowed(false);
        validationDTO.setHourlyRate(BigDecimal.valueOf(10.0));

        // Act
        entryRequestHandler.handleEntry(validationDTO);

        // Assert
        verify(visitorSender).sendEntryResponseToGate(argThat(gateCtrl ->
                gateCtrl.getGateId().equals("Gate1") &&
                gateCtrl.getLotId().equals(1L) &&
                !gateCtrl.getIsValid() &&
                gateCtrl.getIsEntry()
        ));
        verify(visitorDataRepository, never()).save(any());
    }
}
