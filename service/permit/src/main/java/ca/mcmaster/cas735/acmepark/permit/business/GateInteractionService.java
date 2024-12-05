package ca.mcmaster.cas735.acmepark.permit.business;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationRequestDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationResponseDTO;


import ca.mcmaster.cas735.acmepark.permit.business.entity.Permit;
import ca.mcmaster.cas735.acmepark.permit.port.PermitDataRepo;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidationResultSender;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GateInteractionService implements PermitValidator {
    private final PermitDataRepo permitDataRepo;
    private final PermitValidationResultSender permitValidationResultSender;

    @Autowired
    public GateInteractionService(PermitDataRepo permitDataRepo, PermitValidationResultSender permitValidationResultSender) {
        this.permitDataRepo = permitDataRepo;
        this.permitValidationResultSender = permitValidationResultSender;
    }

    @Override
    public void validatePermit(PermitValidationRequestDTO requestDTO) {
        // Retrieve the permit based on transponderId and lotId
        Optional<Permit> permitOptional = permitDataRepo.findByTransponderNumberAndLotId(
                requestDTO.getTransponderId(), requestDTO.getLotId());

        // Check if the permit exists.
        // Check the permit validity based on validFrom, validUntil, and the current timestamp.
        boolean isValid = permitOptional.map(permit -> isPermitValid(permit, requestDTO.getTimestamp())).orElse(false);

        // Prepare the response based on validity.
        PermitValidationResponseDTO responseDTO = new PermitValidationResponseDTO();
        responseDTO.setGateId(requestDTO.getGateId());
        responseDTO.setLotId(requestDTO.getLotId());
        responseDTO.setIsValid(isValid);
        responseDTO.setEntry(requestDTO.isEntry());

        permitValidationResultSender.sendValidationResult(responseDTO);
    }

    private boolean isPermitValid(Permit permit, LocalDateTime currentTimestamp) {


        // Convert validFrom and validUntil from LocalDate to LocalDateTime at the start of the day.
        LocalDateTime validFromDateTime = permit.getValidFrom();
        LocalDateTime validUntilDateTime = permit.getValidUntil();

        // Check if the current timestamp is within the valid period.
        return !currentTimestamp.isBefore(validFromDateTime)
                && !currentTimestamp.isAfter(validUntilDateTime);
    }

}
