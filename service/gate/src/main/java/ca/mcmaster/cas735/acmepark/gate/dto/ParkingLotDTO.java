package ca.mcmaster.cas735.acmepark.gate.dto;

import ca.mcmaster.cas735.acmepark.gate.business.entities.ParkingLot;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ParkingLotDTO {
    private Long lotId;
    private String lotName;
    private int totalSpots;
    private boolean visitorAllowed;
    private BigDecimal hourlyRate;
    private String location;

    public ParkingLotDTO(ParkingLot parkingLot) {
        this.lotId = parkingLot.getLotId();
        this.lotName = parkingLot.getLotName();
        this.totalSpots = parkingLot.getTotalSpots();
        this.visitorAllowed = parkingLot.isVisitorAllowed();
        this.hourlyRate = parkingLot.getHourlyRate();
        this.location = parkingLot.getLocation();
    }
}
