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

    /**
     * Verifies that the `LotOccupancyHandler` class can retrieve the occupancy rate of a parking lot.
     */
    @Test
    void testGetOccupancyRate_Success() throws NotFoundException {
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

        LotOccupancy occupancy = new LotOccupancy();
        occupancy.setLotId(lotId);
        occupancy.setCurrentOccupancy(75);
        occupancy.setTimestamp(LocalDateTime.now());

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