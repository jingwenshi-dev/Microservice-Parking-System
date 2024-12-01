package ca.mcmaster.cas735.acmepark.permit.DTO;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermitRenewalDTO {
    private int permitId;
    private LocalDate validFrom;
    private LocalDate validUntil;
}
