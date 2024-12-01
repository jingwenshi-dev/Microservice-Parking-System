package ca.mcmaster.cas735.acmepark.gate.port.required;

import ca.mcmaster.cas735.acmepark.gate.business.entities.LotOccupancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotOccupancyDataRepo extends JpaRepository<LotOccupancy, Long> {
}
