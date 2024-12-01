package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.business.entities.Voucher;
import ca.mcmaster.cas735.acmepark.payment.business.entities.VoucherRedemption;
import ca.mcmaster.cas735.acmepark.payment.dto.ApplyVoucherDTO;
import ca.mcmaster.cas735.acmepark.payment.dto.VoucherDTO;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.VoucherManager;
import ca.mcmaster.cas735.acmepark.payment.ports.required.VoucherDataRepo;
import ca.mcmaster.cas735.acmepark.payment.ports.required.VoucherRedemptionRepo;
import org.springframework.stereotype.Service;

@Service
public class VoucherProcessor implements VoucherManager {

    private final VoucherDataRepo voucherDataRepo;
    private final VoucherRedemptionRepo voucherRedemptionRepo;

    public VoucherProcessor(VoucherDataRepo voucherDataRepo, VoucherRedemptionRepo voucherRedemptionRepo) {
        this.voucherDataRepo = voucherDataRepo;
        this.voucherRedemptionRepo = voucherRedemptionRepo;
    }

    @Override
    public String createVoucher(VoucherDTO voucherCode) {
        Voucher savedVoucher = voucherDataRepo.saveAndFlush(voucherCode.asVoucher());
        return savedVoucher.getVoucher();
    }

    @Override
    public String applyVoucher(ApplyVoucherDTO voucherCode) {
        VoucherRedemption savedVoucherRedemption = voucherRedemptionRepo.saveAndFlush(voucherCode.asVoucherRedemption());
        return savedVoucherRedemption.getLicensePlate();
    }
}
