package ca.mcmaster.cas735.acmepark.permit.port;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitRenewalDTO;

public interface PermitApplicationPort {
    void applyForPermit(PermitCreatedDTO permitDTO);
    void renewPermit(PermitRenewalDTO renewalDTO);
    int getValidPermitCount();
    void processPaymentSuccess(PermitCreatedDTO event);
}
