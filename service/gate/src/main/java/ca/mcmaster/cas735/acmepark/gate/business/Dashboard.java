package ca.mcmaster.cas735.acmepark.gate.business;

import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.gate.dto.LotOccupancyDTO;
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
    public LotOccupancyDTO getParkingLotStatus(Long lotId) throws NotFoundException {
        return new LotOccupancyDTO(LotOccupancyDB.findLotOccupancyByLotId(lotId)
                .orElseThrow(() -> new NotFoundException("Parking lot not found")));
    }

    @Override
    public void recordOccupancy(Long lotId, Boolean isEntry) {

    }
}
