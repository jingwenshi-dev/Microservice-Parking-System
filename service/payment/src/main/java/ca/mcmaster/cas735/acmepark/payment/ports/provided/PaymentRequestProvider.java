package ca.mcmaster.cas735.acmepark.payment.ports.provided;

import ca.mcmaster.cas735.acmepark.payment.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;


public interface PaymentRequestProvider {
    PaymentRequest buildPaymentRequest(String transponderNumber) throws NotFoundException;
}