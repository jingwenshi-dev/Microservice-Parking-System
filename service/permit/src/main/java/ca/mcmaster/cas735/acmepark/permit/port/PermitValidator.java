package ca.mcmaster.cas735.acmepark.permit.port;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationRequestDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationResponseDTO;

public interface PermitValidator {
    // Method to receive a validation request from the gate and validate the permit
    void validatePermit(PermitValidationRequestDTO requestDTO);
}
