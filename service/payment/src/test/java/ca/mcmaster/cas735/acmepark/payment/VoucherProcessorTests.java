package ca.mcmaster.cas735.acmepark.payment;

import ca.mcmaster.cas735.acmepark.payment.business.VoucherProcessor;
import ca.mcmaster.cas735.acmepark.payment.business.entities.Voucher;
import ca.mcmaster.cas735.acmepark.payment.business.entities.VoucherRedemption;
import ca.mcmaster.cas735.acmepark.payment.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.payment.dto.VoucherDTO;
import ca.mcmaster.cas735.acmepark.payment.ports.required.VoucherDataRepo;
import ca.mcmaster.cas735.acmepark.payment.ports.required.VoucherRedemptionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class VoucherProcessorTests {
    @Mock
    private VoucherDataRepo voucherDataRepo;

    @Mock
    private VoucherRedemptionRepo voucherRedemptionRepo;

    private VoucherProcessor voucherProcessor;

    @BeforeEach
    void setUp() {
        voucherProcessor = new VoucherProcessor(voucherDataRepo, voucherRedemptionRepo);
    }

    @Test
    void testHasValidActiveVoucher_Success() {
        String licensePlate = "ABC123";
        when(voucherRedemptionRepo.findByLicensePlate(licensePlate))
                .thenReturn(Optional.of(new VoucherRedemption()));

        boolean result = voucherProcessor.hasValidActiveVoucher(licensePlate);

        assertTrue(result);
    }

    @Test
    void testHasValidActiveVoucher_Fail() {
        String licensePlate = "ABC123";
        when(voucherRedemptionRepo.findByLicensePlate(licensePlate))
                .thenReturn(Optional.empty());

        boolean result = voucherProcessor.hasValidActiveVoucher(licensePlate);

        assertFalse(result);
    }

    @Test
    void testCreateVoucher_Success() throws Exception {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setVoucher("TEST123");
        voucherDTO.setValidFrom("2024-01-01T00:00:00");
        voucherDTO.setValidUntil("2024-12-31T23:59:59");

        when(voucherDataRepo.findById("TEST123")).thenReturn(Optional.empty());
        when(voucherDataRepo.saveAndFlush(any())).thenReturn(voucherDTO.asVoucher());

        String result = voucherProcessor.createVoucher(voucherDTO);

        assertEquals("TEST123", result);
    }

    @Test
    void testCreateVoucher_InvalidDate() {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setVoucher("TEST123");
        voucherDTO.setValidFrom("2024-12-31T23:59:59");
        voucherDTO.setValidUntil("2024-01-01T00:00:00");

        assertThrows(Exception.class, () -> voucherProcessor.createVoucher(voucherDTO));
    }

    @Test
    void testCreateVoucher_AlreadyExists() {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setVoucher("TEST123");
        voucherDTO.setValidFrom("2024-01-01T00:00:00");
        voucherDTO.setValidUntil("2024-12-31T23:59:59");

        when(voucherDataRepo.findById("TEST123")).thenReturn(Optional.of(new Voucher()));

        assertThrows(AlreadyExistingException.class, () -> voucherProcessor.createVoucher(voucherDTO));
    }
}
