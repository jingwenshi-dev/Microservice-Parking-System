package ca.mcmaster.cas735.acmepark.permit.port;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitRenewalDTO;
import ca.mcmaster.cas735.acmepark.permit.business.errors.NotFoundException;

public interface PermitManager {
    void applyForPermit(PermitCreatedDTO permitDTO) throws NotFoundException;
    void renewPermit(PermitRenewalDTO renewalDTO) throws NotFoundException;
    int getValidPermitCount();
    void processPaymentSuccess(PermitCreatedDTO event);
}
