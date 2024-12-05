package ca.mcmaster.cas735.acmepark.permit;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitRenewalDTO;
import ca.mcmaster.cas735.acmepark.permit.adapter.AMQP.AMQPPaymentSender;
import ca.mcmaster.cas735.acmepark.permit.business.PermitProcessor;
import ca.mcmaster.cas735.acmepark.permit.business.entity.Permit;
import ca.mcmaster.cas735.acmepark.permit.business.entity.User;
import ca.mcmaster.cas735.acmepark.permit.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.permit.port.PermitDataRepo;
import ca.mcmaster.cas735.acmepark.permit.port.UserDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermitProcessorTests {

    @Mock
    private PermitDataRepo permitDataRepo;

    @Mock
    private UserDataRepo userDataRepo;

    @Mock
    private AMQPPaymentSender paymentSender;

    @Captor
    private ArgumentCaptor<Permit> permitCaptor;

    private PermitProcessor permitProcessor;
    private User testUser;
    private Permit testPermit;
    private PermitCreatedDTO testPermitDTO;

    @BeforeEach
    void setUp() {
        permitProcessor = new PermitProcessor(paymentSender, permitDataRepo, userDataRepo);

        // Setup test user
        testUser = new User();
        testUser.setUserId(1);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setUserType(User.UserType.STUDENT);

        // Setup test permit
        testPermit = new Permit();
        testPermit.setPermitId(1);
        testPermit.setTransponderNumber(UUID.randomUUID());
        testPermit.setValidFrom(LocalDateTime.now());
        testPermit.setValidUntil(LocalDateTime.now().plusMonths(6));
        testPermit.setUser(testUser);
        testPermit.setLotId(1L);
        testPermit.setLicensePlate("ABC123");

        // Setup test DTO
        testPermitDTO = new PermitCreatedDTO();
        testPermitDTO.setUserId(1);
        testPermitDTO.setLotId(1L);
        testPermitDTO.setValidFrom(LocalDateTime.now());
        testPermitDTO.setValidUntil(LocalDateTime.now().plusMonths(6));
        testPermitDTO.setLicensePlate("ABC123");
        testPermitDTO.setUserType("STUDENT");
        testPermitDTO.setPaymentMethod("CREDIT_CARD");
    }

    @Test
    void testApplyForPermit_Success() throws NotFoundException {
        // Arrange
        when(userDataRepo.findByUserId(1)).thenReturn(Optional.of(testUser));

        // Act
        permitProcessor.applyForPermit(testPermitDTO);

        // Assert
        verify(paymentSender, times(1)).initiatePayment(any(PermitCreatedDTO.class));
        verify(userDataRepo, times(1)).findByUserId(1);
    }

    @Test
    void testApplyForPermit_UserNotFound() {
        // Arrange
        when(userDataRepo.findByUserId(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> permitProcessor.applyForPermit(testPermitDTO));
        verifyNoInteractions(paymentSender);
    }

    @Test
    void testRenewPermit_Success() throws NotFoundException {
        // Arrange
        PermitRenewalDTO renewalDTO = new PermitRenewalDTO();
        renewalDTO.setPermitId(1);
        renewalDTO.setValidFrom(LocalDateTime.now());
        renewalDTO.setValidUntil(LocalDateTime.now().plusMonths(6));
        renewalDTO.setPaymentMethod("creditCard");

        when(permitDataRepo.findById(1)).thenReturn(Optional.of(testPermit));

        // Act
        permitProcessor.renewPermit(renewalDTO);

        // Assert
        verify(paymentSender, times(1)).initiatePayment(any(PermitCreatedDTO.class));
    }

    @Test
    void renewPermit_PermitNotFound() {
        // Arrange
        PermitRenewalDTO renewalDTO = new PermitRenewalDTO();
        renewalDTO.setPermitId(999);

        when(permitDataRepo.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> permitProcessor.renewPermit(renewalDTO));
        verifyNoInteractions(paymentSender);
    }

    @Test
    void testProcessPayment_ApplySuccess() {
        // Arrange
        testPermitDTO.setResult(true);
        testPermitDTO.setPermitType("APPLY");
        when(userDataRepo.findById(1)).thenReturn(Optional.of(testUser));

        // Act
        permitProcessor.processPaymentSuccess(testPermitDTO);

        // Assert
        verify(permitDataRepo).save(permitCaptor.capture());
        Permit savedPermit = permitCaptor.getValue();
        assertEquals(testPermitDTO.getLicensePlate(), savedPermit.getLicensePlate());
        assertEquals(testPermitDTO.getTransponderNumber(), savedPermit.getTransponderNumber());
        assertEquals(testPermitDTO.getLotId(), savedPermit.getLotId());
    }

    @Test
    void testProcessPayment_RenewalSuccess() {
        // Arrange
        testPermitDTO.setResult(true);
        testPermitDTO.setPermitType("RENEW");

        //Simulate a renewal in the future
        testPermitDTO.setValidFrom(LocalDateTime.now().plusMonths(1));
        testPermitDTO.setValidUntil(LocalDateTime.now().plusMonths(12));

        when(userDataRepo.findById(1)).thenReturn(Optional.of(testUser));
        when(permitDataRepo.findByPermitId(testPermitDTO.getPermitId())).thenReturn(Optional.of(testPermit));

        // Act
        permitProcessor.processPaymentSuccess(testPermitDTO);

        // Assert
        verify(permitDataRepo).save(permitCaptor.capture());
        Permit savedPermit = permitCaptor.getValue();
        assertEquals(testPermitDTO.getValidFrom(), savedPermit.getValidFrom());
        assertEquals(testPermitDTO.getValidUntil(), savedPermit.getValidUntil());
    }

    @Test
    void countValidPermits_Success() {
        // Arrange
        Permit validPermit1 = new Permit();
        validPermit1.setValidFrom(LocalDateTime.now().minusDays(1));
        validPermit1.setValidUntil(LocalDateTime.now().plusDays(1));

        Permit validPermit2 = new Permit();
        validPermit2.setValidFrom(LocalDateTime.now().minusDays(1));
        validPermit2.setValidUntil(LocalDateTime.now().plusDays(1));

        Permit expiredPermit = new Permit();
        expiredPermit.setValidFrom(LocalDateTime.now().minusDays(10));
        expiredPermit.setValidUntil(LocalDateTime.now().minusDays(1));

        when(permitDataRepo.findAll()).thenReturn(Arrays.asList(validPermit1, validPermit2, expiredPermit));

        // Act
        int count = permitProcessor.getValidPermitCount();

        // Assert
        assertEquals(2, count);
    }
}