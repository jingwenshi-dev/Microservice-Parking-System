package ca.mcmaster.cas735.acmepark.permit.DTO;


import lombok.*;

@Data
@Getter
@Setter
public class PermitValidationResponseDTO {
    private String gateId;
    private int lotId;
    private Boolean isValid;
    private Boolean isEntry;

    public PermitValidationResponseDTO() {}
    public PermitValidationResponseDTO(String gateId, int lotId, Boolean isValid, Boolean isEntry) {
        this.gateId = gateId;
        this.lotId = lotId;
        this.isValid = isValid;
        this.isEntry = isEntry;
    }




    public String getGateId() {
        return gateId;
    }
    public Boolean getValid(){
        return isValid;
    }
}
