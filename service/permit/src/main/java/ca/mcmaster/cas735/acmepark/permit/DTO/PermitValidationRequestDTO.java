package ca.mcmaster.cas735.acmepark.permit.DTO;

public class PermitValidationRequestDTO {
    private String transponderId;

    // Default constructor for deserialization
    public PermitValidationRequestDTO() {}

    public PermitValidationRequestDTO(String transponderId) {
        this.transponderId = transponderId;
    }

    public String getTransponderId() {
        return transponderId;
    }

    public void setTransponderId(String transponderId) {
        this.transponderId = transponderId;
    }
}
