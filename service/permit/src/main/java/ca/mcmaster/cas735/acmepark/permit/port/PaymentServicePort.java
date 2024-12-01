package ca.mcmaster.cas735.acmepark.permit.port;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;

public interface PaymentServicePort {
    void initiatePayment(PermitCreatedDTO permit);
}
