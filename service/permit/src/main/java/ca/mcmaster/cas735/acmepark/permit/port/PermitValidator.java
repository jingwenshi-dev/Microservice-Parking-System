package ca.mcmaster.cas735.acmepark.permit.port;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationRequestDTO;

public interface PermitValidator {
    void validatePermit(PermitValidationRequestDTO request);
}
