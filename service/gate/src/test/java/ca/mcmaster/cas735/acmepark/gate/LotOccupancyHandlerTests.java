package ca.mcmaster.cas735.acmepark.gate;

import ca.mcmaster.cas735.acmepark.gate.business.LotOccupancyHandler;
import ca.mcmaster.cas735.acmepark.gate.business.entities.LotOccupancy;
import ca.mcmaster.cas735.acmepark.gate.business.entities.ParkingLot;
import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LotOccupancyHandlerTests {

    @Mock
    private LotOccupancyDataRepo lotOccupancyDB;

    @Mock
    private ParkingLotDataRepo parkingLotDB;

    private LotOccupancyHandler lotOccupancyHandler;

    @BeforeEach
    void setUp() {
        lotOccupancyHandler = new LotOccupancyHandler(lotOccupancyDB, parkingLotDB);
    }

    private ParkingLot createTestParkingLot() {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setLotId(1L);
        parkingLot.setLotName("Test Lot");
        parkingLot.setLocation("Test Location");
        parkingLot.setHourlyRate(new BigDecimal("5.00"));
        parkingLot.setVisitorAllowed(true);
        parkingLot.setTotalSpots(100);
        return parkingLot;
    }

    private LotOccupancy createTestLotOccupancy(Long lotId, int currentOccupancy) {
        LotOccupancy occupancy = new LotOccupancy();
        occupancy.setLotId(lotId);
        occupancy.setCurrentOccupancy(currentOccupancy);
        occupancy.setTimestamp(LocalDateTime.now());
        return occupancy;
    }

    /**
     * Verifies that the `LotOccupancyHandler` class can retrieve the occupancy rate of a parking lot.
     */
    @Test
    void testGetOccupancyRate_Success() throws NotFoundException {
        LotOccupancy occupancy = createTestLotOccupancy(1L, 75);
        ParkingLot parkingLot = createTestParkingLot();

        when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
                .thenReturn(Optional.of(occupancy));
        when(parkingLotDB.findByLotId(1L))
                .thenReturn(Optional.of(parkingLot));

        String result = lotOccupancyHandler.getOccupancyRate(1L);

        assertEquals("75.00%", result);
    }

    /**
     * Verifies that the `LotOccupancyHandler` class can handle a parking lot occupancy not found.
     */
    @Test
    void testGetOccupancyRate_LotOccupancyNotFound() {
        Long lotId = 999L;
        when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(lotId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> lotOccupancyHandler.getOccupancyRate(lotId));
    }

    /**
     * Verifies that the `LotOccupancyHandler` class can handle a parking lot not found.
     */
    @Test
    void testGetOccupancyRate_ParkingLotNotFound() {
        Long lotId = 999L;
        LotOccupancy occupancy = createTestLotOccupancy(lotId, 75);

        when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(lotId))
                .thenReturn(Optional.of(occupancy));
        when(parkingLotDB.findByLotId(lotId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> lotOccupancyHandler.getOccupancyRate(lotId));
    }

    /**
     * Verifies that the `LotOccupancyHandler` class can retrieve the peak hours of a parking lot.
     */
    @Test
    void testGetParkingLotPeakHours_Success() throws NotFoundException {
        Long lotId = 1L;
        LocalDateTime peakTime = LocalDateTime.now();

        LotOccupancy occupancy1 = createTestLotOccupancy(lotId, 50);
        occupancy1.setTimestamp(peakTime.minusHours(1));

        LotOccupancy occupancy2 = createTestLotOccupancy(lotId, 80);
        occupancy2.setTimestamp(peakTime);

        LotOccupancy occupancy3 = createTestLotOccupancy(lotId, 30);
        occupancy3.setTimestamp(peakTime.plusHours(1));

        when(lotOccupancyDB.findAllByLotIdOrderByTimestampAsc(lotId))
                .thenReturn(Optional.of(Arrays.asList(occupancy1, occupancy2, occupancy3)));

        String result = lotOccupancyHandler.getParkingLotPeakHours(lotId);

        assertTrue(result.contains(peakTime.toString()));
        assertTrue(result.contains("80"));
    }

    /**
     * Verifies that the `LotOccupancyHandler` class can handle a parking lot peak hours not found.
     */
    @Test
    void testGetParkingLotPeakHours_NoData() {
        Long lotId = 999L;
        when(lotOccupancyDB.findAllByLotIdOrderByTimestampAsc(lotId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> lotOccupancyHandler.getParkingLotPeakHours(lotId));
    }
}