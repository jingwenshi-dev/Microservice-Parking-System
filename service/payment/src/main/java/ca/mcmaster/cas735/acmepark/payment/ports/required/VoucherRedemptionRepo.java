package ca.mcmaster.cas735.acmepark.payment.ports.required;

import ca.mcmaster.cas735.acmepark.payment.business.entities.VoucherRedemption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRedemptionRepo extends JpaRepository<VoucherRedemption, String> {
}
