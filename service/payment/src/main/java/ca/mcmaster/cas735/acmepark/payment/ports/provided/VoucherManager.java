package ca.mcmaster.cas735.acmepark.payment.ports.provided;

import ca.mcmaster.cas735.acmepark.payment.dto.ApplyVoucherDTO;
import ca.mcmaster.cas735.acmepark.payment.dto.VoucherDTO;

public interface VoucherManager {
    String applyVoucher(ApplyVoucherDTO voucherCode);
    String createVoucher(VoucherDTO voucherCode);
}
