package ca.mcmaster.cas735.acmepark.gate.port.required;

import ca.mcmaster.cas735.acmepark.gate.business.entities.LotOccupancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LotOccupancyDataRepo extends JpaRepository<LotOccupancy, Long> {
    Optional<LotOccupancy> findLotOccupancyByLotId(Long lotId);
}
