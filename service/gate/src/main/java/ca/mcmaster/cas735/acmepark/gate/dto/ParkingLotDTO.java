package ca.mcmaster.cas735.acmepark.gate.dto;

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
}
