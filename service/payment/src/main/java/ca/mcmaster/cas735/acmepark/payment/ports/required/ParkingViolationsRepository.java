package ca.mcmaster.cas735.acmepark.payment.ports.required;

import ca.mcmaster.cas735.acmepark.payment.business.entities.ParkingViolation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingViolationsRepository extends JpaRepository<ParkingViolation, Long> {
    List<ParkingViolation> findByLicensePlateAndIsPaidFalse(String licensePlate);
}