package ca.mcmaster.cas735.acmepark.gate.business;

import ca.mcmaster.cas735.acmepark.gate.port.provided.Monitor;
import ca.mcmaster.cas735.acmepark.gate.port.required.ParkingLotDataRepo;
import org.springframework.stereotype.Service;

@Service
public class Dashboard implements Monitor {

    private final ParkingLotDataRepo parkingLotDB;

    public Dashboard(ParkingLotDataRepo parkingLotDB) {
        this.parkingLotDB = parkingLotDB;
    }

    @Override
    public void getParkingLotStatus(Long lotId) {
        parkingLotDB.findById(lotId);
    }
}
