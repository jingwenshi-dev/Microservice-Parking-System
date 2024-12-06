package ca.mcmaster.cas735.acmepark.permit.port;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;

public interface PaymentSenderPort {
    void initiatePayment(PermitCreatedDTO permit);
}
