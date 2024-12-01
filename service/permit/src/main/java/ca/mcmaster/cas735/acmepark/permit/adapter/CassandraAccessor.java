package ca.mcmaster.cas735.acmepark.permit.adapter;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationRequestDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationResponseDTO;
import ca.mcmaster.cas735.acmepark.permit.port.PermitDBAccessor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CassandraAccessor implements PermitDBAccessor {

    @Override
    public PermitValidationResponseDTO validPermit(PermitValidationRequestDTO requestDTO) {
        // Assuming requestDTO has the transponderId
        String transponderId = requestDTO.getTransponderId();

        // Business logic to check if the transponderId is valid
        boolean isValid = Objects.equals(transponderId, "1");


        // Creating a ResponseDTO object with the validation result
        return new PermitValidationResponseDTO(transponderId, isValid);
    }
}
