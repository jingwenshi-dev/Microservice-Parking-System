package ca.mcmaster.cas735.acmepark.permit;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationRequestDTO;
import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.permit.business.GateInteractionService;
import ca.mcmaster.cas735.acmepark.permit.business.entity.Permit;
import ca.mcmaster.cas735.acmepark.permit.port.PermitDataRepo;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidationResultSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GateInteractionServiceTests {

    @Mock
    private PermitDataRepo permitDataRepo;

    @Mock
    private PermitValidationResultSender validationResultSender;

    @Captor
    private ArgumentCaptor<GateCtrlDTO> responseCaptor;

    private GateInteractionService gateInteractionService;
    private Permit testPermit;

    @BeforeEach
    void setUp() {
        gateInteractionService = new GateInteractionService(permitDataRepo, validationResultSender);
        testPermit = createTestPermit();
    }

    private Permit createTestPermit() {
        Permit permit = new Permit();
        permit.setPermitId(1);
        permit.setTransponderNumber(UUID.randomUUID());
        permit.setValidFrom(LocalDateTime.now().minusDays(1));
        permit.setValidUntil(LocalDateTime.now().plusMonths(6));
        permit.setLotId(1L);
        permit.setLicensePlate("ABC123");
        return permit;
    }

    private PermitValidationRequestDTO createTestRequestDTO(UUID transponderId, Long lotId, LocalDateTime timestamp) {
        PermitValidationRequestDTO requestDTO = new PermitValidationRequestDTO();
        requestDTO.setTransponderId(transponderId);
        requestDTO.setLotId(lotId);
        requestDTO.setTimestamp(timestamp);
        requestDTO.setGateId("GATE_1");
        requestDTO.setEntry(true);
        return requestDTO;
    }

    @Test
    void testValidatePermit_ValidPermit() {
        PermitValidationRequestDTO requestDTO = createTestRequestDTO(testPermit.getTransponderNumber(), testPermit.getLotId(), LocalDateTime.now());

        when(permitDataRepo.findByTransponderNumberAndLotId(requestDTO.getTransponderId(), requestDTO.getLotId()))
                .thenReturn(Optional.of(testPermit));

        gateInteractionService.validatePermit(requestDTO);

        verify(validationResultSender).sendValidationResult(responseCaptor.capture());
        GateCtrlDTO response = responseCaptor.getValue();
        assertTrue(response.getIsValid());
        assertEquals("GATE_1", response.getGateId());
        assertEquals(1L, response.getLotId());
        assertTrue(response.getIsEntry());
    }

    @Test
    void testValidatePermit_ExpiredPermit() {
        testPermit.setValidUntil(LocalDateTime.now().minusDays(1));
        PermitValidationRequestDTO requestDTO = createTestRequestDTO(testPermit.getTransponderNumber(), testPermit.getLotId(), LocalDateTime.now());

        when(permitDataRepo.findByTransponderNumberAndLotId(requestDTO.getTransponderId(), requestDTO.getLotId()))
                .thenReturn(Optional.of(testPermit));

        gateInteractionService.validatePermit(requestDTO);

        verify(validationResultSender).sendValidationResult(responseCaptor.capture());
        GateCtrlDTO response = responseCaptor.getValue();
        assertFalse(response.getIsValid());
    }

    @Test
    void testValidatePermit_PermitNotFound() {
        PermitValidationRequestDTO requestDTO = createTestRequestDTO(UUID.randomUUID(), 1L, LocalDateTime.now());

        when(permitDataRepo.findByTransponderNumberAndLotId(requestDTO.getTransponderId(), requestDTO.getLotId()))
                .thenReturn(Optional.empty());

        gateInteractionService.validatePermit(requestDTO);

        verify(validationResultSender).sendValidationResult(responseCaptor.capture());
        GateCtrlDTO response = responseCaptor.getValue();
        assertFalse(response.getIsValid());
    }

    @Test
    void testValidatePermit_FuturePermit() {
        testPermit.setValidFrom(LocalDateTime.now().plusDays(1));
        PermitValidationRequestDTO requestDTO = createTestRequestDTO(testPermit.getTransponderNumber(), testPermit.getLotId(), LocalDateTime.now());

        when(permitDataRepo.findByTransponderNumberAndLotId(requestDTO.getTransponderId(), requestDTO.getLotId()))
                .thenReturn(Optional.of(testPermit));

        gateInteractionService.validatePermit(requestDTO);

        verify(validationResultSender).sendValidationResult(responseCaptor.capture());
        GateCtrlDTO response = responseCaptor.getValue();
        assertFalse(response.getIsValid());
    }
}