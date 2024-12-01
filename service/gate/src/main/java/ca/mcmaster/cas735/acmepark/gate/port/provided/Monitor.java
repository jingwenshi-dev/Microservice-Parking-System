package ca.mcmaster.cas735.acmepark.gate.port.provided;

import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.gate.dto.LotOccupancyDTO;
import ca.mcmaster.cas735.acmepark.gate.dto.ParkingLotDTO;

public interface Monitor {
    ParkingLotDTO getParkingLotInfo(Long lotID) throws NotFoundException;
    LotOccupancyDTO getParkingLotStatus(Long lotId) throws NotFoundException;
    void recordOccupancy(Long lotId, Boolean isEntry);
}
