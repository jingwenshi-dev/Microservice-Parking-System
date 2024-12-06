package ca.mcmaster.cas735.acmepark.gate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TransponderDTO {
    private String transponderId;
    private String licensePlate;
    private String gateId;
    private Long lotId;
    private boolean isEntry;
    private LocalDateTime timestamp = LocalDateTime.now();
}
