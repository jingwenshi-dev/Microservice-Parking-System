package ca.mcmaster.cas735.acmepark.gate.business;

import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.gate.dto.LotOccupancyDTO;
import ca.mcmaster.cas735.acmepark.gate.dto.ParkingLotDTO;
import ca.mcmaster.cas735.acmepark.gate.port.provided.Monitor;
import ca.mcmaster.cas735.acmepark.gate.port.required.LotOccupancyDataRepo;
import ca.mcmaster.cas735.acmepark.gate.port.required.ParkingLotDataRepo;
import org.springframework.stereotype.Service;

@Service
public class Dashboard implements Monitor {

    private final ParkingLotDataRepo parkingLotDB;
    private final LotOccupancyDataRepo lotOccupancyDB;

    public Dashboard(ParkingLotDataRepo parkingLotDB, LotOccupancyDataRepo lotOccupancyDB) {
        this.parkingLotDB = parkingLotDB;
        this.lotOccupancyDB = lotOccupancyDB;
    }

    @Override
    public ParkingLotDTO getParkingLotInfo(Long lotId) throws NotFoundException {
        return new ParkingLotDTO(parkingLotDB.findById(lotId)
                .orElseThrow(() -> new NotFoundException(lotId.toString())));
    }

    @Override
    public LotOccupancyDTO getParkingLotStatus(Long lotId) throws NotFoundException {
        return new LotOccupancyDTO(lotOccupancyDB.findLotOccupancyByLotId(lotId)
                .orElseThrow(() -> new NotFoundException(lotId.toString())));
    }

    @Override
    public void recordOccupancy(Long lotId, Boolean isEntry) {

    }
}
