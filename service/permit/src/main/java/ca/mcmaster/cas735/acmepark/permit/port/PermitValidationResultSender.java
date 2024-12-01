package ca.mcmaster.cas735.acmepark.permit.port;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationResponseDTO;

public interface PermitValidationResultSender {
    void sendValidationResult(PermitValidationResponseDTO response);
}
