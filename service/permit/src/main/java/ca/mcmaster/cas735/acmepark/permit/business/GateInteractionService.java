package ca.mcmaster.cas735.acmepark.permit.business;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationRequestDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationResponseDTO;


import ca.mcmaster.cas735.acmepark.permit.business.entity.Permit;
import ca.mcmaster.cas735.acmepark.permit.port.PermitRepository;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidationResultSender;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GateInteractionService{
    private final PermitRepository permitRepository;
    private final PermitValidationResultSender permitValidationResultSender;


    @Autowired
    public GateInteractionService(PermitRepository permitRepository, PermitValidationResultSender
            permitValidationResultSender) {
        this.permitRepository = permitRepository;
        this.permitValidationResultSender = permitValidationResultSender;
    }

    public void validatePermit(PermitValidationRequestDTO requestDTO) {
        // Retrieve the permit based on transponderId and lotId
        Optional<Permit> permitOptional = permitRepository.findByTransponderNumberAndLotId(requestDTO.getTransponderId(), requestDTO.getLotId());

        // Check if the permit exists.
        // Check the permit validity based on validFrom, validUntil, and the current timestamp.
        boolean isValid = permitOptional.map(permit -> isPermitValid(permit, requestDTO.getTimestamp()))
                .orElse(false);

        // Prepare the response based on validity.
        PermitValidationResponseDTO responseDTO = new PermitValidationResponseDTO(
                requestDTO.getGateId(),
                requestDTO.getLotId(),
                isValid,
                requestDTO.getIsEntry()
        );

        permitValidationResultSender.sendValidationResult(responseDTO);
    }

    private boolean isPermitValid(Permit permit, String timestamp) {
        // Convert the timestamp from the gate service to LocalDateTime for comparison.
        LocalDateTime currentTimestamp = LocalDateTime.parse(timestamp);

        // Convert validFrom and validUntil from LocalDate to LocalDateTime at the start of the day.
        LocalDateTime validFromDateTime = permit.getValidFrom().atStartOfDay();
        LocalDateTime validUntilDateTime = permit.getValidUntil().atStartOfDay();

        // Check if the current timestamp is within the valid period.
        return !currentTimestamp.isBefore(validFromDateTime) && !currentTimestamp.isAfter(validUntilDateTime);
    }



}
