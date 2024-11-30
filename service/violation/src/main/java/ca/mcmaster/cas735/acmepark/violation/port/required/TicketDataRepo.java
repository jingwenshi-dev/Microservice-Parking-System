package ca.mcmaster.cas735.acmepark.violation.port.required;

import ca.mcmaster.cas735.acmepark.payment.business.entities.ParkingViolation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TicketDataRepo extends JpaRepository<ParkingViolation, UUID>{
    Optional<ParkingViolation> findByViolationIdAndLicensePlate(UUID violationId, String licensePlate);
}
