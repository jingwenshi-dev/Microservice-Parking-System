package ca.mcmaster.cas735.acmepark.gate;

import ca.mcmaster.cas735.acmepark.gate.business.Dashboard;
import ca.mcmaster.cas735.acmepark.gate.business.GateReqHandler;
import ca.mcmaster.cas735.acmepark.gate.business.GateResultHandler;
import ca.mcmaster.cas735.acmepark.gate.business.entities.LotOccupancy;
import ca.mcmaster.cas735.acmepark.gate.business.entities.ParkingLot;
import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.gate.dto.TransponderDTO;
import ca.mcmaster.cas735.acmepark.gate.port.GateController;
import ca.mcmaster.cas735.acmepark.gate.port.ValidationReqSender;
import ca.mcmaster.cas735.acmepark.gate.port.provided.LotOccupancyPort;
import ca.mcmaster.cas735.acmepark.gate.port.required.LotOccupancyDataRepo;
import ca.mcmaster.cas735.acmepark.gate.port.required.ParkingLotDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GateApplicationTests {

	@Mock
	private ValidationReqSender validationReqSender;

	@Mock
	private GateController gateController;

	@Mock
	private ParkingLotDataRepo parkingLotDB;

	@Mock
	private LotOccupancyDataRepo lotOccupancyDB;

	@Mock
	private LotOccupancyPort lotOccupancyPort;

	private GateReqHandler gateReqHandler;
	private GateResultHandler gateResultHandler;
	private Dashboard dashboard;

	@BeforeEach
	void setUp() throws Exception {
		try (var _ = MockitoAnnotations.openMocks(this)) {
			gateReqHandler = new GateReqHandler(validationReqSender);
			dashboard = new Dashboard(parkingLotDB, lotOccupancyDB, lotOccupancyPort);
			gateResultHandler = new GateResultHandler(gateController, dashboard);
		}
	}

	/**
	 * Verifies when a transponder is read, `GateReqHandler` triggers the `readTransponder` method on the `gateReqHandler`.
	 * Verifies `readTransponder` method calls the `send` method of the `validationReqSender` mock object.
	 * Verifies that `send` is called exactly once with the transponder as an argument.
	 */
	@Test
	void testGateReqHandler_SendsValidationRequest() {
		// Arrange
		TransponderDTO transponder = new TransponderDTO();
		transponder.setTransponderId("");
		transponder.setLicensePlate("ABC123");
		transponder.setGateId("G1");
		transponder.setLotId(1L);
		transponder.setEntry(true);

		// Act
		gateReqHandler.readTransponder(transponder);

		// Assert
		verify(validationReqSender, times(1)).send(transponder);
	}

	/**
	 * Verifies that the `Dashboard` class correctly retrieves the information
	 * of a parking lot when the `getParkingLotInfo` method is called with a valid lot ID.
	 */
	@Test
	void testDashboard_GetParkingLotInfo_Success() throws NotFoundException {
		// Arrange
		ParkingLot parkingLot = new ParkingLot();
		parkingLot.setLotId(1L);
		parkingLot.setLotName("Test Lot");
		parkingLot.setTotalSpots(100);
		parkingLot.setVisitorAllowed(true);
		parkingLot.setHourlyRate(new BigDecimal("5.00"));
		parkingLot.setLocation("Test Location");

		when(parkingLotDB.findByLotId(1L)).thenReturn(Optional.of(parkingLot));

		// Act & Assert
		assertDoesNotThrow(() -> dashboard.getParkingLotInfo(1L));
		assertEquals(1L, dashboard.getParkingLotInfo(1L).getLotId());
		assertEquals("Test Lot", dashboard.getParkingLotInfo(1L).getLotName());
		assertEquals(100, dashboard.getParkingLotInfo(1L).getTotalSpots());
		assertTrue(dashboard.getParkingLotInfo(1L).isVisitorAllowed());
		assertEquals(new BigDecimal("5.00"), dashboard.getParkingLotInfo(1L).getHourlyRate());
		assertEquals("Test Location", dashboard.getParkingLotInfo(1L).getLocation());
	}

	/**
	 * Verifies that the `Dashboard` class throws a `NotFoundException` when the `getParkingLotInfo` method is called with an invalid lot ID.
	 */
	@Test
	void testDashboard_GetParkingLotInfo_NotFound() {
		// Arrange
		when(parkingLotDB.findByLotId(999L)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(NotFoundException.class, () -> dashboard.getParkingLotInfo(999L));
	}

	/**
	 * Verifies that the `Dashboard` class correctly retrieves the occupancy information
	 * of a parking lot when the `getParkingLotOccupancy` method is called with a valid lot ID.
	 */
	@Test
	void testDashboard_GetParkingLotStatus_Success() throws NotFoundException {
		// Arrange
		LotOccupancy currentOccupancy = new LotOccupancy();
		currentOccupancy.setLotId(1L);
		currentOccupancy.setCurrentOccupancy(50);
		currentOccupancy.setTimestamp(LocalDateTime.now());

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
				.thenReturn(Optional.of(currentOccupancy));
		when(lotOccupancyPort.getOccupancyRate(1L)).thenReturn("50%");
		when(lotOccupancyPort.getParkingLotPeakHours(1L)).thenReturn("12:00 PM - 2:00 PM");

		// Act
		var lotOccupancyDTO = dashboard.getParkingLotStatus(1L);

		// Assert
		assertEquals(1L, lotOccupancyDTO.getLotId());
		assertEquals(50, lotOccupancyDTO.getCurrentOccupancy());
		assertEquals("50%", lotOccupancyDTO.getOccupancyRate());
		assertEquals("12:00 PM - 2:00 PM", lotOccupancyDTO.getPeakingHour());
	}

	/**
	 * Verifies that the `Dashboard` class throws a `NotFoundException` when the `getParkingLotStatus` method is called with an invalid lot ID.
	 */
	@Test
	void testDashboard_GetParkingLotStatus_NotFound() {
		// Arrange
		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(999L))
				.thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(NotFoundException.class, () -> dashboard.getParkingLotStatus(999L));
	}

	/**
	 * Verifies that the `Dashboard` class correctly records the occupancy information exactly once
	 * of a parking lot when the `recordOccupancy` method is called with a valid lot ID and entry.
	 */
	@Test
	void testDashboard_RecordOccupancy_Success() {
		// Arrange
		LotOccupancy currentOccupancy = new LotOccupancy();
		currentOccupancy.setId(1L);
		currentOccupancy.setLotId(1L);
		currentOccupancy.setCurrentOccupancy(50);
		currentOccupancy.setTimestamp(LocalDateTime.now());

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
				.thenReturn(Optional.of(currentOccupancy));
		when(lotOccupancyDB.save(any(LotOccupancy.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		// Act & Assert
		assertDoesNotThrow(() -> dashboard.recordOccupancy(1L, true));
		verify(lotOccupancyDB, times(1)).save(any(LotOccupancy.class));
	}

	/**
	 * Verifies that the `Dashboard` class throws an `IllegalArgumentException` when the current occupancy
	 * will be negative after recording an exit.
	 */
	@Test
	void testDashboard_RecordOccupancy_InvalidExit() {
		// Arrange
		LotOccupancy currentOccupancy = new LotOccupancy();
		currentOccupancy.setLotId(1L);
		currentOccupancy.setCurrentOccupancy(0);
		currentOccupancy.setTimestamp(LocalDateTime.now());

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
				.thenReturn(Optional.of(currentOccupancy));

		// Act & Assert
		assertThrows(IllegalArgumentException.class,
				() -> dashboard.recordOccupancy(1L, false));
	}

	/**
	 * Verifies that the `GateResultHandler` class correctly sends a valid gate control result exactly once.
	 * Verifies that the occurrence of the current occupancy is recorded exactly once by `monitor`.
	 */
	@Test
	void testGateResultHandler_ReceiveValidResult() throws NotFoundException {
		// Arrange
		GateCtrlDTO gateCtrl = new GateCtrlDTO();
		gateCtrl.setGateId("G1");
		gateCtrl.setLotId(1L);
		gateCtrl.setIsValid(true);
		gateCtrl.setIsEntry(true);
		gateCtrl.setQrCode("Some QR Code");

		LotOccupancy currentOccupancy = new LotOccupancy();
		currentOccupancy.setId(1L);
		currentOccupancy.setLotId(1L);
		currentOccupancy.setCurrentOccupancy(50);
		currentOccupancy.setTimestamp(LocalDateTime.now());

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
				.thenReturn(Optional.of(currentOccupancy));
		when(lotOccupancyDB.save(any(LotOccupancy.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		gateResultHandler.receive(gateCtrl);

		// Assert
		verify(gateController, times(1)).gateControl(gateCtrl);
		verify(lotOccupancyDB, times(1)).save(any(LotOccupancy.class));
	}

	/**
	 * Verifies that the `GateResultHandler` class can handle a gate control with invalid parking lot id.
	 */
	@Test
	void testGateResultHandler_ReceiveInvalidLotId() {
		// Arrange
		GateCtrlDTO gateCtrl = new GateCtrlDTO();
		gateCtrl.setGateId("G1");
		gateCtrl.setLotId(999L);
		gateCtrl.setIsValid(true);
		gateCtrl.setIsEntry(true);

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(999L)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(NotFoundException.class, () -> gateResultHandler.receive(gateCtrl));
	}

	/**
	 * Verifies that the `GateResultHandler` class can handle a gate control result with invalid exit.
	 */
	@Test
	void testGateResultHandler_ReceiveInvalidExit() {
		// Arrange
		GateCtrlDTO gateCtrl = new GateCtrlDTO();
		gateCtrl.setGateId("G1");
		gateCtrl.setLotId(1L);
		gateCtrl.setIsValid(true);
		gateCtrl.setIsEntry(false);
		gateCtrl.setQrCode("Some QR Code");

		LotOccupancy currentOccupancy = new LotOccupancy();
		currentOccupancy.setId(1L);
		currentOccupancy.setLotId(1L);
		currentOccupancy.setCurrentOccupancy(0);
		currentOccupancy.setTimestamp(LocalDateTime.now());

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
				.thenReturn(Optional.of(currentOccupancy));

		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> gateResultHandler.receive(gateCtrl));
	}
}