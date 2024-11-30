package ca.mcmaster.cas735.acmepark.payment.ports.provided;

import ca.mcmaster.cas735.acmepark.payment.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;

import java.util.UUID;


public interface PaymentRequestProvider {
    PaymentRequest buildPaymentRequest(UUID transponderNumber) throws NotFoundException;
}