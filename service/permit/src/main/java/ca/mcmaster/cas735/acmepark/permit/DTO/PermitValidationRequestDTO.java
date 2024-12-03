package ca.mcmaster.cas735.acmepark.permit.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PermitValidationRequestDTO {
    private UUID transponderId;
    private String gateId;
    private int lotId;
    private Boolean isEntry;
    private String timestamp;
}
