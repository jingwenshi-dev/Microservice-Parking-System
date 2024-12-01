package ca.mcmaster.cas735.acmepark.gate.business;

import ca.mcmaster.cas735.acmepark.gate.port.provided.Monitor;
import ca.mcmaster.cas735.acmepark.gate.port.required.LotOccupancyDataRepo;
import org.springframework.stereotype.Service;

@Service
public class Dashboard implements Monitor {

    private final LotOccupancyDataRepo LotOccupancyDB;

    public Dashboard(LotOccupancyDataRepo parkingLotDB) {
        this.LotOccupancyDB = parkingLotDB;
    }

    @Override
    public void getParkingLotStatus(Long lotId) {
        LotOccupancyDB.findLotOccupancyByLotId(lotId);
    }
}
