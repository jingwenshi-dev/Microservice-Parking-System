package ca.mcmaster.cas735.acmepark.permit.port;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitRenewalDTO;

public interface PermitApplicationPort {
    void applyForPermit(PermitCreatedDTO permitDTO) throws Exception;
    void renewPermit(PermitRenewalDTO renewalDTO) throws Exception;
}
