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
        // 获取最新的占用数据
        LotOccupancy lotOccupancy = lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(lotId)
                .orElseThrow(() -> new NotFoundException("LotOccupancy not found for lotId: " + lotId));
        int cutrrentOccpuancy =lotOccupancy.getCurrentOccupancy();

        // 获取停车场总车位数
        int totalSpots = parkingLotDB.findByLotId(lotId)
                .map(ParkingLot::getTotalSpots)
                .orElseThrow(() -> new NotFoundException("ParkingLot not found for lotId: " + lotId));

        // 返回带有占用率的
        double occupancyRate = totalSpots > 0
                ? ((double)cutrrentOccpuancy / (double) totalSpots) * 100
                : 0.0;
        return String.format("%.2f%%", occupancyRate);
    }

    @Override
    public String getParkingLotPeakHours(Long lotId) throws NotFoundException {
        // 从数据库中获取当前停车场的所有占用记录
        List<LotOccupancy> occupancies = lotOccupancyDB.findAllByLotIdOrderByTimestampAsc(lotId)
                .orElseThrow(() -> new NotFoundException("No occupancy data found for lotId:" + lotId));

        // 简化示例：找到最大占用的时间段
        LotOccupancy peakOccupancy = occupancies.stream()
                .max(Comparator.comparingInt(LotOccupancy::getCurrentOccupancy))
                .orElseThrow(() -> new NotFoundException("Could not determine peak hours for lotId: " + lotId));

        return String.format("Peak hours: %s with %d vehicles", peakOccupancy.getTimestamp(), peakOccupancy.getCurrentOccupancy());
    }
}
