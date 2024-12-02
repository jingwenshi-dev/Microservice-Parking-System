package ca.mcmaster.cas735.acmepark.permit.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermitValidationResponseDTO {
    private String gateId;
    private int lotId;
    private Boolean isValid;
    private Boolean isEntry;
}
