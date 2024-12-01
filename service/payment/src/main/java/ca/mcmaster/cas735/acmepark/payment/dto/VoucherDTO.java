package ca.mcmaster.cas735.acmepark.payment.dto;

import ca.mcmaster.cas735.acmepark.payment.business.entities.Voucher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class VoucherDTO {

    @NotBlank(message = "Voucher is required")
    private String voucher;

    @NotNull(message = "Valid from date is required")
    private LocalDate validFrom;

    @NotNull(message = "Valid until date is required")
    private LocalDate validUntil;

    public Voucher asVoucher() {
        Voucher voucher = new Voucher();
        voucher.setVoucher(this.voucher);
        voucher.setValidFrom(this.validFrom);
        voucher.setValidUntil(this.validUntil);
        return voucher;
    }
}
