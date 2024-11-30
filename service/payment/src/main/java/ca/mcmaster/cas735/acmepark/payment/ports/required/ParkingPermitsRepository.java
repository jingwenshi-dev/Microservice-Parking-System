package ca.mcmaster.cas735.acmepark.payment.ports.required;

import ca.mcmaster.cas735.acmepark.payment.business.entities.ParkingPermits;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ParkingPermitsRepository extends JpaRepository<ParkingPermits, String> {
    Optional<ParkingPermits> findByTransponderNumber(String transponderNumber);
}