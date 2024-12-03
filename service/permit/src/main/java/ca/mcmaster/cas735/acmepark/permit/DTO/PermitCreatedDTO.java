package ca.mcmaster.cas735.acmepark.permit.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PermitCreatedDTO {

    private UUID transponderNumber;
    private LocalDateTime validFrom;  // Changed to LocalDate
    private LocalDateTime validUntil;
    private int userId;
    private Long lotId;
    private boolean result;
    private String licensePlate;
    private String userType;
    private String paymentMethod;
    private String permitType;

    public PermitCreatedDTO(LocalDateTime validFrom, LocalDateTime validUntil, int userId, String licensePlate, Long lotId, String paymentMethod) {
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.licensePlate = licensePlate;
        this.lotId = lotId;
    }

}
