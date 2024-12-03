package ca.mcmaster.cas735.acmepark.permit.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermitValidationResponseDTO {
    private String gateId;
    private int lotId;
    private Boolean isValid;
    private Boolean isEntry;

    public PermitValidationResponseDTO(String gateId, int lotId, Boolean isValid, Boolean isEntry) {
        this.gateId = gateId;
        this.lotId = lotId;
        this.isValid = isValid;
        this.isEntry = isEntry;
    }
}
