package ca.mcmaster.cas735.acmepark.permit.DTO;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
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


    // No-argument constructor
    public PermitCreatedDTO() {}

    public PermitCreatedDTO(LocalDateTime validFrom, LocalDateTime validUntil, int userId, String licensePlate, Long lotId, String paymentMethod) {
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.licensePlate = licensePlate;
        this.lotId = lotId;
    }
    public String getLicensePlate() {
        return this.licensePlate;
    }
    public boolean isResult() {
        return this.result;
    }

    public int getUserId() {
        return userId;
    }

    public void setTransponderNumber(UUID transponderNumber) {
        this.transponderNumber = transponderNumber;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }



    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UUID getTransponderNumber() {
        return transponderNumber;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public Long getLotId() {
        return lotId;
    }

    public String getPermitType() {return permitType;}

    public void setPermitType(String permitType) {this.permitType = permitType;}

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

}
