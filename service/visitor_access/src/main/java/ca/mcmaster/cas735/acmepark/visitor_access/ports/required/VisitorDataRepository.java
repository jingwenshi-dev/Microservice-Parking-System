package ca.mcmaster.cas735.acmepark.visitor_access.ports.required;

import ca.mcmaster.cas735.acmepark.visitor_access.business.entities.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitorDataRepository extends JpaRepository<Visitor, Long> {
    Optional<Visitor> findFirstByLicensePlateOrderByEntryTimeDesc(String licensePlate);
}