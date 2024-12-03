package ca.mcmaster.cas735.acmepark.permit.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PermitValidationRequestDTO {
    private UUID transponderId;
    private String gateId;
    private Long lotId;
    private boolean isEntry;
    private LocalDateTime timestamp;
}
