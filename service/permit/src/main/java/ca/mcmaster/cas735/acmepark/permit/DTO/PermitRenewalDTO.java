package ca.mcmaster.cas735.acmepark.permit.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermitRenewalDTO {
    private int permitId;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

    public int getPermitId() {
        return permitId;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }
}
