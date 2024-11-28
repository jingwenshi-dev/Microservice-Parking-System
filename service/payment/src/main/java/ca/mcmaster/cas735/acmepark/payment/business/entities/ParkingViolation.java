package ca.mcmaster.cas735.acmepark.payment.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "parking_violations")
public class ParkingViolation {

    @Id
    private Long violationId;

    private LocalDateTime violationTime;

    private String licensePlate;

    private BigDecimal fineAmount;

    private Boolean isPaid;

    private Long officerId;

    private Long lotId;
}