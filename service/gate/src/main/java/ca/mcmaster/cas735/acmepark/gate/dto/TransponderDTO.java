package ca.mcmaster.cas735.acmepark.gate.dto;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
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
    private boolean visitorAllowed;
    private BigDecimal hourlyRate;
    private String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
}
