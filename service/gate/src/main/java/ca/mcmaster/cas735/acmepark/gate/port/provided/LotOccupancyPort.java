package ca.mcmaster.cas735.acmepark.gate.port.provided;

import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;


public interface LotOccupancyPort {
    String getOccupancyRate(Long lotId) throws NotFoundException;

    String getParkingLotPeakHours(Long lotId) throws NotFoundException;
}
