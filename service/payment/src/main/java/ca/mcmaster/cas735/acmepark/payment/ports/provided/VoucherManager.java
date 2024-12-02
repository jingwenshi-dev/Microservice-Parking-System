package ca.mcmaster.cas735.acmepark.payment.ports.provided;

import ca.mcmaster.cas735.acmepark.payment.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.payment.business.errors.InvalidDateException;
import ca.mcmaster.cas735.acmepark.payment.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.payment.business.errors.VoucherExpiredException;
import ca.mcmaster.cas735.acmepark.payment.dto.ApplyVoucherDTO;
import ca.mcmaster.cas735.acmepark.payment.dto.VoucherDTO;

public interface VoucherManager {
    String createVoucher(VoucherDTO voucherCode) throws AlreadyExistingException, InvalidDateException;
    String applyVoucher(ApplyVoucherDTO voucherCode) throws NotFoundException, VoucherExpiredException;
}
