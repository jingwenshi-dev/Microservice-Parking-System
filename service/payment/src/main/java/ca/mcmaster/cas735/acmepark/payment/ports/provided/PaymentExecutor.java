package ca.mcmaster.cas735.acmepark.payment.ports.provided;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;

public interface PaymentExecutor {
    boolean executePayment(PaymentRequest paymentRequest);
}
