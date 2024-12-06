package ca.mcmaster.cas735.acmepark.permit.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermitValidationResponseDTO {
    private String gateId;
    private Long lotId;
    private Boolean isValid;
    private boolean isEntry;
}
