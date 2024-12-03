package ca.mcmaster.cas735.acmepark.permit.port;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;

public interface PaymentListenerPort {
    void processPaymentSuccess(PermitCreatedDTO event);
}
