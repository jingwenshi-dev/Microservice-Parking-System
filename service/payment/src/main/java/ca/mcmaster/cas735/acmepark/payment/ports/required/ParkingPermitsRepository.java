package ca.mcmaster.cas735.acmepark.payment.ports.required;

import ca.mcmaster.cas735.acmepark.payment.business.entities.ParkingPermits;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface ParkingPermitsRepository extends JpaRepository<ParkingPermits, UUID> {
    Optional<ParkingPermits> findByTransponderNumber(UUID transponderNumber);
}