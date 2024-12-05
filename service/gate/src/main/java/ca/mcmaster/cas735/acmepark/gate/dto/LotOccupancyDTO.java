package ca.mcmaster.cas735.acmepark.gate.dto;

import ca.mcmaster.cas735.acmepark.gate.business.entities.LotOccupancy;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LotOccupancyDTO {
    private Long id;
    private Long lotId;
    private LocalDateTime timestamp;
    private int currentOccupancy;
    private String occupancyRate;
    private String peakingHour;

    public LotOccupancyDTO(LotOccupancy lot) {
        this.id = lot.getId();
        this.lotId = lot.getLotId();
        this.timestamp = lot.getTimestamp();
        this.currentOccupancy = lot.getCurrentOccupancy();
    }
}
