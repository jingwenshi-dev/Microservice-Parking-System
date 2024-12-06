package ca.mcmaster.cas735.acmepark.payment.ports.provided;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;

public interface PaymentHandler {
    void handlePayment(PaymentRequest paymentRequest);
}