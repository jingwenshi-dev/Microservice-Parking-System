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
    private String licensePlate;
    private String userType;
    private String paymentMethod;



    public PermitCreatedDTO(LocalDate validFrom, LocalDate validUntil, int userId, String licensePlate, int lotId, String userType, String paymentMethod) {
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.licensePlate = licensePlate;
        this.lotId = lotId;
        this.userType = userType;
    }

}
