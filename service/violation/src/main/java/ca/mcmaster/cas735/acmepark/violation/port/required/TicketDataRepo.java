package ca.mcmaster.cas735.acmepark.violation.port.required;

import ca.mcmaster.cas735.acmepark.violation.business.entities.ParkingViolation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketDataRepo extends JpaRepository<ParkingViolation, String>{
    public Optional<ParkingViolation> findByViolationIdAndLicensePlate(long violationId, String licensePlate);
}
