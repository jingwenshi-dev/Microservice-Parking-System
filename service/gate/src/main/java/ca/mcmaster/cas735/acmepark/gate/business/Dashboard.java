package ca.mcmaster.cas735.acmepark.gate.business;

import ca.mcmaster.cas735.acmepark.gate.business.entities.LotOccupancy;
import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.gate.dto.LotOccupancyDTO;
import ca.mcmaster.cas735.acmepark.gate.dto.ParkingLotDTO;
import ca.mcmaster.cas735.acmepark.gate.port.provided.Monitor;
import ca.mcmaster.cas735.acmepark.gate.port.required.LotOccupancyDataRepo;
import ca.mcmaster.cas735.acmepark.gate.port.required.ParkingLotDataRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        return new ParkingLotDTO(parkingLotDB.findByLotId(lotId)
                .orElseThrow(() -> new NotFoundException(lotId.toString())));
    }

    @Override
    public LotOccupancyDTO getParkingLotStatus(Long lotId) throws NotFoundException, IllegalArgumentException {
        return new LotOccupancyDTO(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(lotId)
                .orElseThrow(() -> new NotFoundException(lotId.toString())));
    }

    @Override
    public void recordOccupancy(Long lotId, Boolean isEntry) throws IllegalArgumentException, NotFoundException {
        // 查询当前 lotId 的最新记录
        LotOccupancy latestOccupancy = lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(lotId)
                .orElseThrow(() -> new NotFoundException("Lot ID not found: " + lotId));

        // 根据 isEntry 增加或减少 currentOccupancy
        int newOccupancy = latestOccupancy.getCurrentOccupancy() + (isEntry ? 1 : -1);

        // 防止 currentOccupancy 小于 0
        if (newOccupancy < 0) {
            throw new IllegalArgumentException("Current occupancy cannot be negative.");
        }

        // 创建新的记录
        LotOccupancy newRecord = new LotOccupancy();
        newRecord.setLotId(lotId);
        newRecord.setTimestamp(LocalDateTime.now());
        newRecord.setCurrentOccupancy(newOccupancy);
        // 保存新记录到数据库
        lotOccupancyDB.save(newRecord);
    }
}
