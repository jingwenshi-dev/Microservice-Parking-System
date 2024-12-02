package ca.mcmaster.cas735.acmepark.permit.business.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "Permit")
public class Permit {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int permitId;

    private UUID transponderNumber;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // Assuming the 'Users' table has 'user_id' as the PK
    private User user;

    private int lotId;
    private String licensePlate;


    public Permit(UUID transponderNumber, LocalDateTime validFrom, LocalDateTime validUntil,User user, int lotId, String licensePlate) {
        this.transponderNumber = transponderNumber;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.user = user;
        this.lotId = lotId;
        this.licensePlate = licensePlate;
    }

  // Default constructor for JPA
    public Permit() {

    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }
    public LocalDateTime getValidUntil() {
        return validUntil;
    }
    public int getPermitId() {
        return permitId;
    }

    public UUID getTransponderNumber() {
        return transponderNumber;
    }

    public User getUser() {
        return user;
    }

    public int getLotId() {
        return lotId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
}
