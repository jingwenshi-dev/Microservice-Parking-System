package ca.mcmaster.cas735.acmepark.payment.ports;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequestDto;

public interface PaymentServicePort {
    public boolean processPayment(PaymentRequestDto paymentRequest);
}
