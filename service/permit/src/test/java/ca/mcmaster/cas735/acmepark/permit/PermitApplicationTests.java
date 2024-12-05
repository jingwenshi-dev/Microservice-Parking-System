package ca.mcmaster.cas735.acmepark.permit;

import ca.mcmaster.cas735.acmepark.permit.DTO.*;
import ca.mcmaster.cas735.acmepark.permit.business.GateInteractionService;
import ca.mcmaster.cas735.acmepark.permit.business.PermitApplicationService;
import ca.mcmaster.cas735.acmepark.permit.business.entity.Permit;
import ca.mcmaster.cas735.acmepark.permit.business.entity.User;
import ca.mcmaster.cas735.acmepark.permit.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.permit.port.PermitRepository;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidationResultSender;
import ca.mcmaster.cas735.acmepark.permit.port.UserRepository;
import ca.mcmaster.cas735.acmepark.permit.adapter.AMQP.AMQPPaymentSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class PermitApplicationTests {

	@Mock
	private PermitRepository permitRepository;

	@Mock
	private PermitValidationResultSender validationResultSender;

	@Mock
	private UserRepository userRepository;

	@Mock
	private AMQPPaymentSender paymentSender;

	private GateInteractionService gateInteractionService;
	private PermitApplicationService permitApplicationService;

	private static final UUID VALID_TRANSPONDER = UUID.randomUUID();
	private static final Long LOT_ID = 1L;
	private static final String GATE_ID = "GATE-001";

	@BeforeEach
	void setUp() {
		gateInteractionService = new GateInteractionService(permitRepository, validationResultSender);
		permitApplicationService = new PermitApplicationService(paymentSender, permitRepository, userRepository);
	}

	@Test
	void validatePermit_ValidPermit_ReturnsTrue() {
		// Arrange
		LocalDateTime now = LocalDateTime.now();
		Permit validPermit = new Permit();
		validPermit.setTransponderNumber(VALID_TRANSPONDER);
		validPermit.setValidFrom(now.minusDays(1));
		validPermit.setValidUntil(now.plusDays(30));
		validPermit.setLotId(LOT_ID);

		PermitValidationRequestDTO request = new PermitValidationRequestDTO();
		request.setTransponderId(VALID_TRANSPONDER);
		request.setGateId(GATE_ID);
		request.setLotId(LOT_ID);
		request.setTimestamp(now);
		request.setEntry(true);

		when(permitRepository.findByTransponderNumberAndLotId(VALID_TRANSPONDER, LOT_ID))
				.thenReturn(Optional.of(validPermit));

		// Act
		gateInteractionService.validatePermit(request);

		// Assert
		verify(validationResultSender).sendValidationResult(argThat(response ->
				response.getIsValid() &&
						response.getGateId().equals(GATE_ID) &&
						response.getLotId().equals(LOT_ID)
		));
	}

	@Test
	void validatePermit_ExpiredPermit_ReturnsFalse() {
		// Arrange
		LocalDateTime now = LocalDateTime.now();
		Permit expiredPermit = new Permit();
		expiredPermit.setTransponderNumber(VALID_TRANSPONDER);
		expiredPermit.setValidFrom(now.minusDays(60));
		expiredPermit.setValidUntil(now.minusDays(30));
		expiredPermit.setLotId(LOT_ID);

		PermitValidationRequestDTO request = new PermitValidationRequestDTO();
		request.setTransponderId(VALID_TRANSPONDER);
		request.setGateId(GATE_ID);
		request.setLotId(LOT_ID);
		request.setTimestamp(now);
		request.setEntry(true);

		when(permitRepository.findByTransponderNumberAndLotId(VALID_TRANSPONDER, LOT_ID))
				.thenReturn(Optional.of(expiredPermit));

		// Act
		gateInteractionService.validatePermit(request);

		// Assert
		verify(validationResultSender).sendValidationResult(argThat(response ->
				!response.getIsValid() &&
						response.getGateId().equals(GATE_ID) &&
						response.getLotId().equals(LOT_ID)
		));
	}

	@Test
	void validatePermit_NonexistentPermit_ReturnsFalse() {
		// Arrange
		LocalDateTime now = LocalDateTime.now();
		PermitValidationRequestDTO request = new PermitValidationRequestDTO();
		request.setTransponderId(VALID_TRANSPONDER);
		request.setGateId(GATE_ID);
		request.setLotId(LOT_ID);
		request.setTimestamp(now);
		request.setEntry(true);

		when(permitRepository.findByTransponderNumberAndLotId(VALID_TRANSPONDER, LOT_ID))
				.thenReturn(Optional.empty());

		// Act
		gateInteractionService.validatePermit(request);

		// Assert
		verify(validationResultSender).sendValidationResult(argThat(response ->
				!response.getIsValid() &&
						response.getGateId().equals(GATE_ID) &&
						response.getLotId().equals(LOT_ID)
		));
	}

	@Test
	void applyForPermit_ValidApplication_InitiatesPayment() {
		// Arrange
		LocalDateTime now = LocalDateTime.now();
		int userId = 1;
		User user = new User();
		user.setUserId(userId);
		user.setUserType(User.UserType.FACULTY);

		PermitCreatedDTO permitDTO = new PermitCreatedDTO();
		permitDTO.setUserId(userId);
		permitDTO.setValidFrom(now);
		permitDTO.setValidUntil(now.plusMonths(6));
		permitDTO.setLotId(LOT_ID);
		permitDTO.setLicensePlate("ABC123");
		permitDTO.setPaymentMethod("CREDIT_CARD");

		when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

		// Act
		permitApplicationService.applyForPermit(permitDTO);

		// Assert
		verify(paymentSender).initiatePayment(argThat(dto ->
				dto.getUserId() == userId &&
						dto.getLotId().equals(LOT_ID) &&
						dto.getUserType().equals("FACULTY") &&
						dto.getPermitType().equals("APPLY")
		));
	}

	@Test
	void applyForPermit_NonexistentUser_ThrowsNotFoundException() {
		// Arrange
		LocalDateTime now = LocalDateTime.now();
		int userId = 999;
		PermitCreatedDTO permitDTO = new PermitCreatedDTO();
		permitDTO.setUserId(userId);
		permitDTO.setValidFrom(now);
		permitDTO.setValidUntil(now.plusMonths(6));

		when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(NotFoundException.class, () ->
				permitApplicationService.applyForPermit(permitDTO)
		);
		verify(paymentSender, never()).initiatePayment(any());
	}

	@Test
	void countValidPermits_ReturnsCorrectCount() {
		// Arrange
		LocalDateTime now = LocalDateTime.now();
		Permit validPermit1 = new Permit();
		validPermit1.setValidFrom(now.minusDays(1));
		validPermit1.setValidUntil(now.plusDays(30));

		Permit validPermit2 = new Permit();
		validPermit2.setValidFrom(now.minusDays(15));
		validPermit2.setValidUntil(now.plusDays(15));

		Permit expiredPermit = new Permit();
		expiredPermit.setValidFrom(now.minusDays(60));
		expiredPermit.setValidUntil(now.minusDays(30));

		when(permitRepository.findAll()).thenReturn(Arrays.asList(validPermit1, validPermit2, expiredPermit));

		// Act
		int validCount = permitApplicationService.countValidPermits();

		// Assert
		assertEquals(2, validCount);
	}
}