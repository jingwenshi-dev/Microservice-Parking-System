package ca.mcmaster.cas735.acmepark.violation;

import ca.mcmaster.cas735.acmepark.violation.business.TicketProcessor;
import ca.mcmaster.cas735.acmepark.violation.business.entities.ParkingViolation;
import ca.mcmaster.cas735.acmepark.violation.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.violation.port.required.TicketDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketProcessorTests {

    @Mock
    private TicketDataRepo ticketDataRepo;

    @InjectMocks
    private TicketProcessor ticketProcessor;

    private ParkingViolation violation;
    private TicketDTO ticketDTO;
    private final String TEST_LICENSE_PLATE = "ABC123";

    @BeforeEach
    void setUp() {
        // Initialize test violation
        violation = new ParkingViolation();
        violation.setViolationId(UUID.randomUUID());
        violation.setLicensePlate(TEST_LICENSE_PLATE);
        violation.setFineAmount(new BigDecimal("50.00"));
        violation.setOfficerId(1L);
        violation.setLotId(1L);
        violation.setViolationTime(LocalDateTime.now());

        // Initialize test DTO
        ticketDTO = new TicketDTO();
        ticketDTO.setTicketNum(UUID.randomUUID());
        ticketDTO.setLicensePlate(TEST_LICENSE_PLATE);
        ticketDTO.setFineAmount(new BigDecimal("50.00"));
        ticketDTO.setOfficerId(1L);
        ticketDTO.setLotId(1L);
        ticketDTO.setViolationTime(LocalDateTime.now());
    }

    /**
     * Verifies that the lookupTicket method returns a list of TicketDTO objects when the license plate is found.
     * Verifies that the findAllByLicensePlate method is called once.
     */
    @Test
    void testLookupTicket_Success() throws NotFoundException {
        // Arrange
        when(ticketDataRepo.findAllByLicensePlate(TEST_LICENSE_PLATE))
                .thenReturn(Collections.singletonList(violation));

        // Act
        List<TicketDTO> result = ticketProcessor.lookupTicket(TEST_LICENSE_PLATE);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TEST_LICENSE_PLATE, result.getFirst().getLicensePlate());
        verify(ticketDataRepo, times(1)).findAllByLicensePlate(TEST_LICENSE_PLATE);
    }

    /**
     * Verifies that the lookupTicket method throws a NotFoundException when the license plate is not found.
     */
    @Test
    void testLookupTicket_NotFound() {
        // Arrange
        when(ticketDataRepo.findAllByLicensePlate(TEST_LICENSE_PLATE))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(NotFoundException.class, () ->
                ticketProcessor.lookupTicket(TEST_LICENSE_PLATE)
        );
        verify(ticketDataRepo, times(1)).findAllByLicensePlate(TEST_LICENSE_PLATE);
    }

    /**
     * Verifies that the issueTicket method returns a UUID when the ticket is successfully issued.
     * Verifies that the saveAndFlush method is called once.
     */
    @Test
    void testIssueTicket_Success() {
        // Arrange
        when(ticketDataRepo.saveAndFlush(any(ParkingViolation.class)))
                .thenReturn(violation);

        // Act
        UUID result = ticketProcessor.issueTicket(ticketDTO);

        // Assert
        assertNotNull(result);
        verify(ticketDataRepo, times(1)).saveAndFlush(any(ParkingViolation.class));
    }

    /**
     * Verifies that the deleteTickets method successfully deletes all tickets for a given license plate.
     * Verifies that the deleteAllByLicensePlate method is called once.
     */
    @Test
    void testDeleteTickets_Success() {
        // Arrange
        doNothing().when(ticketDataRepo).deleteAllByLicensePlate(TEST_LICENSE_PLATE);

        // Act
        ticketProcessor.deleteTickets(TEST_LICENSE_PLATE);

        // Assert
        verify(ticketDataRepo, times(1)).deleteAllByLicensePlate(TEST_LICENSE_PLATE);
    }
}