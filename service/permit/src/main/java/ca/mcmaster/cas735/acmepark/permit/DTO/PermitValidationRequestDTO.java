package ca.mcmaster.cas735.acmepark.permit.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermitValidationRequestDTO {
    private String transponderId;
    private String gateId;
    private int lotId;
    private Boolean isEntry;
    private String timestamp;
}
