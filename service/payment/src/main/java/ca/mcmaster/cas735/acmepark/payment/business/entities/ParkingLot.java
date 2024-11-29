package ca.mcmaster.cas735.acmepark.payment.business.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "PARKING_LOTS")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long lotId;

    private String lotName;
    private int totalSpots;
    private boolean visitorAllowed;
    private BigDecimal hourlyRate;
    private String location;
}