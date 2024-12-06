package ca.mcmaster.cas735.acmepark.payment.ports.provided;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;

public interface TicketDeleteSender {
    void sendTicketDelete(PaymentRequest paymentRequest);
}
