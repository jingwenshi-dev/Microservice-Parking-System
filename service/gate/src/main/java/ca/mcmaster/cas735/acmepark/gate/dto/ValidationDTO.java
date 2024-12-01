package ca.mcmaster.cas735.acmepark.gate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ValidationDTO {
    private String transponderId;
    private String licensePlate;
    private String gateId;
    private boolean isEntry;
    private String timestamp;

    private Long lotId;
    private boolean visitorAllowed;
    private BigDecimal hourlyRate;

    public ValidationDTO(TransponderDTO transponder, ParkingLotDTO parkingLot) {
        this.transponderId = transponder.getTransponderId();
        this.licensePlate = transponder.getLicensePlate();
        this.gateId = transponder.getGateId();
        this.isEntry = transponder.isEntry();
        this.timestamp = transponder.getTimestamp();

        this.lotId = parkingLot.getLotId();
        this.visitorAllowed = parkingLot.isVisitorAllowed();
        this.hourlyRate = parkingLot.getHourlyRate();
    }

}
