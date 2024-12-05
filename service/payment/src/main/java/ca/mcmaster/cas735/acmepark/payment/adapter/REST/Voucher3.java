package ca.mcmaster.cas735.acmepark.payment.adapter.REST;

import ca.mcmaster.cas735.acmepark.payment.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.payment.business.errors.InvalidDateException;
import ca.mcmaster.cas735.acmepark.payment.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.payment.business.errors.VoucherExpiredException;
import ca.mcmaster.cas735.acmepark.payment.dto.ApplyVoucherDTO;
import ca.mcmaster.cas735.acmepark.payment.dto.VoucherDTO;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.VoucherManager;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@Tag(name = "Visitor voucher")
@RequestMapping(value = "/voucher")
public class Voucher3 {

    private final VoucherManager manager;

    @Autowired
    public Voucher3(VoucherManager manager) {
        this.manager = manager;
    }

    @PostMapping(value = "/apply-voucher")
    @Operation(description = "Apply a visitor pass/voucher for your vehicle")
    public ResponseEntity<String> apply(@Valid @RequestBody ApplyVoucherDTO voucher) throws NotFoundException, VoucherExpiredException {
        String licencePlate = manager.applyVoucher(voucher);
        return ResponseEntity.ok(String.format("Voucher successfully applied to: %s", licencePlate));
    }

    @PostMapping(value = "/create")
    @Operation(description = "Create a voucher by Admin")
    public ResponseEntity<String> issue(@Valid @RequestBody VoucherDTO voucher) throws AlreadyExistingException, InvalidDateException {
        String voucherCode = manager.createVoucher(voucher);
        return ResponseEntity.ok(String.format("Voucher successfully created with code: %s", voucherCode));
    }

}
