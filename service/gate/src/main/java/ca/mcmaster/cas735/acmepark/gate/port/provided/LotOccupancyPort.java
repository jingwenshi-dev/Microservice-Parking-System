package ca.mcmaster.cas735.acmepark.gate.port.provided;

import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;


public interface LotOccupancyPort {
    String getOccupancyRate(Long lotId) throws NotFoundException;

    // 新增方法用于后续实现其他功能，如计算高峰期
    String getParkingLotPeakHours(Long lotId) throws NotFoundException;
}
