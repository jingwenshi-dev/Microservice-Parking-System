package ca.mcmaster.cas735.acmepark.payment.ports.required;

import ca.mcmaster.cas735.acmepark.payment.business.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherDataRepo extends JpaRepository<Voucher, String> {
}
