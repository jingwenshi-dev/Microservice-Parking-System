package ca.mcmaster.cas735.acmepark.payment.ports;


import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequestDto;

public interface PaymentProcessor {
    boolean processPayment(PaymentRequestDto paymentRequest);
}