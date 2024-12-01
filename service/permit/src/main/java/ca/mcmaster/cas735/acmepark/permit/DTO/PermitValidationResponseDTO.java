package ca.mcmaster.cas735.acmepark.permit.DTO;



public class PermitValidationResponseDTO {
    private String transponderId;
    private boolean isValid;

    public PermitValidationResponseDTO() {}
    public PermitValidationResponseDTO(String transponderId, boolean isValid) {
        this.transponderId = transponderId;
        this.isValid = isValid;
    }

    public String getTransponderId() {
        return transponderId;
    }

    public void setTransponderId(String transponderId) {
        this.transponderId = transponderId;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

}
