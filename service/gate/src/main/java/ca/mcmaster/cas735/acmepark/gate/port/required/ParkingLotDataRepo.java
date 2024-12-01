package ca.mcmaster.cas735.acmepark.gate.port.required;

import ca.mcmaster.cas735.acmepark.gate.business.entities.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotDataRepo extends JpaRepository<ParkingLot, Long> {
}
