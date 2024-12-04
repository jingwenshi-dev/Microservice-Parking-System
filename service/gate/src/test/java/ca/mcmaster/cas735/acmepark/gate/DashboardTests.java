package ca.mcmaster.cas735.acmepark.gate;

import ca.mcmaster.cas735.acmepark.gate.business.Dashboard;
import ca.mcmaster.cas735.acmepark.gate.business.entities.LotOccupancy;
import ca.mcmaster.cas735.acmepark.gate.business.entities.ParkingLot;
import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardTests {

    @Mock
    private ParkingLotDataRepo parkingLotDB;

    @Mock
    private LotOccupancyDataRepo lotOccupancyDB;

    @Mock
    private LotOccupancyPort lotOccupancyPort;

    private Dashboard dashboard;

    @BeforeEach
    void setUp() {
        dashboard = new Dashboard(parkingLotDB, lotOccupancyDB, lotOccupancyPort);
    }

    /**
     * Verifies that the `Dashboard` class correctly retrieves the information
     * of a parking lot when the `getParkingLotInfo` method is called with a valid lot ID.
     */
    @Test
    void testGetParkingLotInfo_Success() throws NotFoundException {
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
    void testGetParkingLotInfo_NotFound() {
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
    void testGetParkingLotStatus_Success() throws NotFoundException {
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
    void testGetParkingLotStatus_NotFound() {
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
    void testRecordOccupancy_Success() {
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
    void testRecordOccupancy_InvalidExit() {
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
}