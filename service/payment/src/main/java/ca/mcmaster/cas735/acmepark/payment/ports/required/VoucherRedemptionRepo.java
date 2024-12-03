package ca.mcmaster.cas735.acmepark.payment.ports.required;

import ca.mcmaster.cas735.acmepark.payment.business.entities.VoucherRedemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherRedemptionRepo extends JpaRepository<VoucherRedemption, String> {
    Optional<VoucherRedemption> findByLicensePlate(String licensePlate);
}
