package ca.mcmaster.cas735.acmepark.violation.port.required;

import ca.mcmaster.cas735.acmepark.violation.business.entities.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotDataRepo extends JpaRepository<ParkingLot, Long> {
}
