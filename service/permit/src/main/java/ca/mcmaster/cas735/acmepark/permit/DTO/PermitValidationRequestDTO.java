package ca.mcmaster.cas735.acmepark.permit.DTO;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PermitValidationRequestDTO {
    private UUID transponderId;
    private String gateId;
    private int lotId;
    private Boolean isEntry;
    private String timestamp;

    public UUID getTransponderId() {
        return transponderId;
    }

    public int getLotId() {
        return lotId;
    }

    public String timestamp() {
        return timestamp;
    }

    public Boolean getEntry() {
        return isEntry;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getGateId() {
        return gateId;
    }



}
