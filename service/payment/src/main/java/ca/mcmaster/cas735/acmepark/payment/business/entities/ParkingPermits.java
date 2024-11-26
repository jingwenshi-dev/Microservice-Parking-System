package ca.mcmaster.cas735.acmepark.payment.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "parking_permits")
public class ParkingPermits {
    @Id
    private String transponderNumber;

    private LocalDate validFrom;

    private LocalDate validUntil;

    private String licensePlate;

    private String paymentMethod;

    private String payrollNum;

}
