package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.business.entities.Voucher;
import ca.mcmaster.cas735.acmepark.payment.business.entities.VoucherRedemption;
import ca.mcmaster.cas735.acmepark.payment.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.payment.business.errors.InvalidDateException;
import ca.mcmaster.cas735.acmepark.payment.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.payment.business.errors.VoucherExpiredException;
import ca.mcmaster.cas735.acmepark.payment.dto.ApplyVoucherDTO;
import ca.mcmaster.cas735.acmepark.payment.dto.VoucherDTO;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.VoucherManager;
import ca.mcmaster.cas735.acmepark.payment.ports.required.VoucherDataRepo;
import ca.mcmaster.cas735.acmepark.payment.ports.required.VoucherRedemptionRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class VoucherProcessor implements VoucherManager {

    private final VoucherDataRepo voucherDataRepo;
    private final VoucherRedemptionRepo voucherRedemptionRepo;

    public VoucherProcessor(VoucherDataRepo voucherDataRepo, VoucherRedemptionRepo voucherRedemptionRepo) {
        this.voucherDataRepo = voucherDataRepo;
        this.voucherRedemptionRepo = voucherRedemptionRepo;
    }

    @Override
    public String createVoucher(VoucherDTO voucherCode) throws AlreadyExistingException, InvalidDateException {
        if (voucherDataRepo.findById(voucherCode.getVoucher()).isPresent()) {
            throw new AlreadyExistingException("Voucher", voucherCode.getVoucher());
        }

        if (voucherCode.getValidFrom().isAfter(voucherCode.getValidUntil())) {
            throw new InvalidDateException("Voucher valid from date must be before valid until date");
        }

        Voucher savedVoucher = voucherDataRepo.saveAndFlush(voucherCode.asVoucher());
        return savedVoucher.getVoucher();
    }

    @Override
    public String applyVoucher(ApplyVoucherDTO voucherCode) throws NotFoundException, VoucherExpiredException {
        Optional<Voucher> voucher = voucherDataRepo.findById(voucherCode.getVoucher());
        if (voucher.isEmpty()) {
            throw new NotFoundException("Voucher", voucherCode.getVoucher());
        }

        if (voucher.get().getValidUntil().isBefore(LocalDate.now())) {
            throw new VoucherExpiredException("Voucher", voucherCode.getVoucher());
        }

        VoucherRedemption savedVoucherRedemption = voucherRedemptionRepo.saveAndFlush(voucherCode.asVoucherRedemption());
        return savedVoucherRedemption.getLicensePlate();
    }
}
