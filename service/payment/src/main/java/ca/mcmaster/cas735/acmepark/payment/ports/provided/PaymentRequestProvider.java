package ca.mcmaster.cas735.acmepark.payment.ports.provided;

import ca.mcmaster.cas735.acmepark.payment.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PaymentRequestProvider {
    PaymentRequest buildPaymentRequest(String transponderNumber, LocalDateTime entryTime, LocalDateTime exitTime,
                                       BigDecimal hourlyRate) throws ChangeSetPersister.NotFoundException, NotFoundException;
}