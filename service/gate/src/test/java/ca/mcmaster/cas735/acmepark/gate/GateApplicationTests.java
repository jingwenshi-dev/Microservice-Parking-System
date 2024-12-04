package ca.mcmaster.cas735.acmepark.gate;

import ca.mcmaster.cas735.acmepark.gate.business.Dashboard;
import ca.mcmaster.cas735.acmepark.gate.business.GateReqHandler;
import ca.mcmaster.cas735.acmepark.gate.business.GateResultHandler;
import ca.mcmaster.cas735.acmepark.gate.business.LotOccupancyHandler;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
	private LotOccupancyHandler lotOccupancyHandler;

	@BeforeEach
	void setUp() {
		dashboard = new Dashboard(parkingLotDB, lotOccupancyDB, lotOccupancyPort);
		gateReqHandler = new GateReqHandler(validationReqSender);
		gateResultHandler = new GateResultHandler(gateController, dashboard);
		lotOccupancyHandler = new LotOccupancyHandler(lotOccupancyDB, parkingLotDB);
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
		LotOccupancy occupancy = new LotOccupancy();
		occupancy.setLotId(1L);
		occupancy.setCurrentOccupancy(50);
		occupancy.setTimestamp(LocalDateTime.now());

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
				.thenReturn(Optional.of(occupancy));
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
		LotOccupancy occupancy = new LotOccupancy();
		occupancy.setLotId(1L);
		occupancy.setCurrentOccupancy(50);
		occupancy.setTimestamp(LocalDateTime.now());

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
				.thenReturn(Optional.of(occupancy));
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
		LotOccupancy occupancy = new LotOccupancy();
		occupancy.setLotId(1L);
		occupancy.setCurrentOccupancy(0);
		occupancy.setTimestamp(LocalDateTime.now());

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
				.thenReturn(Optional.of(occupancy));

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

		LotOccupancy occupancy = new LotOccupancy();
		occupancy.setLotId(1L);
		occupancy.setCurrentOccupancy(50);
		occupancy.setTimestamp(LocalDateTime.now());

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
				.thenReturn(Optional.of(occupancy));
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

		LotOccupancy occupancy = new LotOccupancy();
		occupancy.setLotId(1L);
		occupancy.setCurrentOccupancy(0);
		occupancy.setTimestamp(LocalDateTime.now());

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
				.thenReturn(Optional.of(occupancy));

		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> gateResultHandler.receive(gateCtrl));
	}

	/**
	 * Verifies that the `LotOccupancyHandler` class can retrieve the occupancy rate of a parking lot.
	 */
	@Test
	void getOccupancyRate_Success() throws NotFoundException {
		// Arrange
		LotOccupancy occupancy = new LotOccupancy();
		occupancy.setLotId(1L);
		occupancy.setCurrentOccupancy(75);
		occupancy.setTimestamp(LocalDateTime.now());

		ParkingLot parkingLot = new ParkingLot();
		parkingLot.setLotId(1L);
		parkingLot.setLotName("Test Lot");
		parkingLot.setLocation("Test Location");
		parkingLot.setHourlyRate(new BigDecimal("5.00"));
		parkingLot.setVisitorAllowed(true);
		parkingLot.setTotalSpots(100);

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
				.thenReturn(Optional.of(occupancy));
		when(parkingLotDB.findByLotId(1L))
				.thenReturn(Optional.of(parkingLot));

		// Act
		String result = lotOccupancyHandler.getOccupancyRate(1L);

		// Assert
		assertEquals("75.00%", result);
	}

	/**
	 * Verifies that the `LotOccupancyHandler` class can handle a parking lot occupancy not found.
	 */
	@Test
	void getOccupancyRate_LotOccupancyNotFound() {
		// Arrange
		Long lotId = 999L;
		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(lotId))
				.thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(NotFoundException.class, () -> lotOccupancyHandler.getOccupancyRate(lotId));
	}

	/**
	 * Verifies that the `LotOccupancyHandler` class can handle a parking lot not found.
	 */
	@Test
	void getOccupancyRate_ParkingLotNotFound() {
		// Arrange
		Long lotId = 999L;

		LotOccupancy occupancy = new LotOccupancy();
		occupancy.setLotId(lotId);
		occupancy.setCurrentOccupancy(75);
		occupancy.setTimestamp(LocalDateTime.now());

		when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(lotId))
				.thenReturn(Optional.of(occupancy));
		when(parkingLotDB.findByLotId(lotId))
				.thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(NotFoundException.class, () -> lotOccupancyHandler.getOccupancyRate(lotId));
	}

	/**
	 * Verifies that the `LotOccupancyHandler` class can retrieve the peak hours of a parking lot.
	 */
	@Test
	void getParkingLotPeakHours_Success() throws NotFoundException {
		// Arrange
		Long lotId = 1L;
		LocalDateTime peakTime = LocalDateTime.now();

		LotOccupancy occupancy1 = new LotOccupancy();
		occupancy1.setLotId(lotId);
		occupancy1.setCurrentOccupancy(50);
		occupancy1.setTimestamp(peakTime.minusHours(1));

		LotOccupancy occupancy2 = new LotOccupancy();
		occupancy2.setLotId(lotId);
		occupancy2.setCurrentOccupancy(80);
		occupancy2.setTimestamp(peakTime);

		LotOccupancy occupancy3 = new LotOccupancy();
		occupancy3.setLotId(lotId);
		occupancy3.setCurrentOccupancy(30);
		occupancy3.setTimestamp(peakTime.plusHours(1));

		when(lotOccupancyDB.findAllByLotIdOrderByTimestampAsc(lotId))
				.thenReturn(Optional.of(Arrays.asList(occupancy1, occupancy2, occupancy3)));

		// Act
		String result = lotOccupancyHandler.getParkingLotPeakHours(lotId);

		// Assert
		assertTrue(result.contains(peakTime.toString()));
		assertTrue(result.contains("80"));
	}

	/**
	 * Verifies that the `LotOccupancyHandler` class can handle a parking lot peak hours not found.
	 */
	@Test
	void getParkingLotPeakHours_NoData() {
		// Arrange
		Long lotId = 999L;
		when(lotOccupancyDB.findAllByLotIdOrderByTimestampAsc(lotId))
				.thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(NotFoundException.class, () -> lotOccupancyHandler.getParkingLotPeakHours(lotId));
	}
}