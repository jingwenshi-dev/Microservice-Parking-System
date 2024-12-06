package ca.mcmaster.cas735.acmepark.gate.business;

import ca.mcmaster.cas735.acmepark.gate.business.entities.LotOccupancy;
import ca.mcmaster.cas735.acmepark.gate.business.entities.ParkingLot;
import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.gate.port.provided.LotOccupancyPort;
import ca.mcmaster.cas735.acmepark.gate.port.required.LotOccupancyDataRepo;
import ca.mcmaster.cas735.acmepark.gate.port.required.ParkingLotDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Service
public class LotOccupancyHandler implements LotOccupancyPort {
    private final LotOccupancyDataRepo lotOccupancyDB;
    private final ParkingLotDataRepo parkingLotDB;

    @Autowired
    public LotOccupancyHandler(LotOccupancyDataRepo lotOccupancyDB, ParkingLotDataRepo parkingLotDB) {
        this.lotOccupancyDB = lotOccupancyDB;
        this.parkingLotDB = parkingLotDB;
    }

    @Override
    public String getOccupancyRate(Long lotId) throws NotFoundException {
        // Get the latest occupancy data
        LotOccupancy lotOccupancy = lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(lotId)
                .orElseThrow(() -> new NotFoundException("LotOccupancy not found for lotId: " + lotId));
        int cutrrentOccpuancy =lotOccupancy.getCurrentOccupancy();

        // Get the total number of parking spaces in the parking lot
        int totalSpots = parkingLotDB.findByLotId(lotId)
                .map(ParkingLot::getTotalSpots)
                .orElseThrow(() -> new NotFoundException("ParkingLot not found for lotId: " + lotId));

        // Returns the occupancy rate of the
        double occupancyRate = totalSpots > 0
                ? ((double)cutrrentOccpuancy / (double) totalSpots) * 100
                : 0.0;
        return String.format("%.2f%%", occupancyRate);
    }

    @Override
    public String getParkingLotPeakHours(Long lotId) throws NotFoundException {
        // Get all current parking lot occupancy records from the database
        List<LotOccupancy> occupancies = lotOccupancyDB.findAllByLotIdOrderByTimestampAsc(lotId)
                .orElseThrow(() -> new NotFoundException("No occupancy data found for lotId:" + lotId));

        // Find the maximum occupied time period
        LotOccupancy peakOccupancy = occupancies.stream()
                .max(Comparator.comparingInt(LotOccupancy::getCurrentOccupancy))
                .orElseThrow(() -> new NotFoundException("Could not determine peak hours for lotId: " + lotId));

        return String.format("Peak hours: %s with %d vehicles",
                peakOccupancy.getTimestamp(),
                peakOccupancy.getCurrentOccupancy());
    }
}
