package ca.mcmaster.cas735.acmepark.permit.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PermitRenewalDTO {
    private int permitId;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private String paymentMethod;
}
