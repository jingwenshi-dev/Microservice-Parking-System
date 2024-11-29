package ca.mcmaster.cas735.acmepark.payment.business.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "PARKING_PERMITS")
public class ParkingPermits {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transponderNumber;

    private LocalDate validFrom;
    private LocalDate validUntil;
    private String licensePlate;
    private String paymentMethod;
    private String payrollNum;
}
