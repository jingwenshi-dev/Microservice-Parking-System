package ca.mcmaster.cas735.acmepark.payment.dto;

import ca.mcmaster.cas735.acmepark.payment.business.entities.Voucher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoucherDTO {

    @NotBlank(message = "Voucher is required")
    private String voucher;

    @NotNull(message = "Valid from date is required")
    private String validFrom;

    @NotNull(message = "Valid until date is required")
    private String validUntil;

    public Voucher asVoucher() {
        Voucher voucher = new Voucher();
        voucher.setVoucher(this.voucher);
        voucher.setValidFrom(this.validFrom);
        voucher.setValidUntil(this.validUntil);
        return voucher;
    }
}
