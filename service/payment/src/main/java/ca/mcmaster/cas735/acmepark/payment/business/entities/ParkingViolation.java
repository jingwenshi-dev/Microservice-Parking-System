package ca.mcmaster.cas735.acmepark.payment.business.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "PARKING_VIOLATIONS")
public class ParkingViolation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID violationId;

    private LocalDateTime violationTime;

    private String licensePlate;

    private BigDecimal fineAmount;

    private Boolean isPaid;

    private Long officerId;

    private Long lotId;
}