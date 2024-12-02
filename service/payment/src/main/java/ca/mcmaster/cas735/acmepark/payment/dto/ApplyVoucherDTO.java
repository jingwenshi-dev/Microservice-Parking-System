package ca.mcmaster.cas735.acmepark.payment.dto;

import ca.mcmaster.cas735.acmepark.payment.business.entities.VoucherRedemption;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplyVoucherDTO {

    @NotBlank(message = "Voucher is required")
    private String voucher;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    public VoucherRedemption asVoucherRedemption() {
        VoucherRedemption voucherRedemption = new VoucherRedemption();
        voucherRedemption.setVoucher(this.voucher);
        voucherRedemption.setLicensePlate(this.licensePlate);
        return voucherRedemption;
    }

}
