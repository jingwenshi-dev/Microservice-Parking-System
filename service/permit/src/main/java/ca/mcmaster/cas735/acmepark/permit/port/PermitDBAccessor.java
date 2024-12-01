package ca.mcmaster.cas735.acmepark.permit.port;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationRequestDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationResponseDTO;

public interface PermitDBAccessor {
    PermitValidationResponseDTO validPermit(PermitValidationRequestDTO requestDTO);
}
