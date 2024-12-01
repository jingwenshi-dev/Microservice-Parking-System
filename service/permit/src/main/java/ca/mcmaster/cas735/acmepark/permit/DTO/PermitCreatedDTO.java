package ca.mcmaster.cas735.acmepark.permit.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PermitCreatedDTO {
    private int permitId;
    private String transponderNumber;
    private LocalDate validFrom;  // Changed to LocalDate
    private LocalDate validUntil;
    private int userId;
    private int lotId;
    private boolean result;
    private final String userType = "permit";
    private String paymentMethod;



    public PermitCreatedDTO(LocalDate validFrom, LocalDate validUntil, int userId, int lotId) {
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.lotId = lotId;
    }

}
