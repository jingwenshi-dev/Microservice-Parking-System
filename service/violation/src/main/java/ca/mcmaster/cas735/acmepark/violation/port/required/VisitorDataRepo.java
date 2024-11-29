package ca.mcmaster.cas735.acmepark.violation.port.required;

import ca.mcmaster.cas735.acmepark.violation.business.entities.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorDataRepo extends JpaRepository<Visitor, String> {
}
