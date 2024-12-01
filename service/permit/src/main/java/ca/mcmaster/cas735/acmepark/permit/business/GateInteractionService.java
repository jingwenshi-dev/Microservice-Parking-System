package ca.mcmaster.cas735.acmepark.permit.business;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationRequestDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationResponseDTO;
import ca.mcmaster.cas735.acmepark.permit.adapter.AMQPValidationResultSender;
import ca.mcmaster.cas735.acmepark.permit.port.PermitDBAccessor;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateInteractionService implements PermitValidator {
    private final PermitDBAccessor permitDBAccessor;
    private final AMQPValidationResultSender amqpValidationResultSender;

    @Autowired
    public GateInteractionService(PermitDBAccessor permitDBAccessor, AMQPValidationResultSender amqpValidationResultSender) {
        this.permitDBAccessor = permitDBAccessor;
        this.amqpValidationResultSender = amqpValidationResultSender;
    }

    @Override
    public void validatePermit(PermitValidationRequestDTO request) {
        PermitValidationResponseDTO response = permitDBAccessor.validPermit(request);
        amqpValidationResultSender.sendValidationResult(response);
    }
}
