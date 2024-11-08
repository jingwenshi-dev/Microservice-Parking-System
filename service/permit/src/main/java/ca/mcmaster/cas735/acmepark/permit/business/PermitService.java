package ca.mcmaster.cas735.acmepark.permit.business;

import ca.mcmaster.cas735.acmepark.permit.adapter.AMQPValidationResultSender;
import ca.mcmaster.cas735.acmepark.permit.port.PermitDBAccessor;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermitService implements PermitValidator {
    private final PermitDBAccessor permitDBAccessor;
    private final AMQPValidationResultSender amqpValidationResultSender;

    @Autowired
    public PermitService(PermitDBAccessor permitDBAccessor, AMQPValidationResultSender amqpValidationResultSender) {
        this.permitDBAccessor = permitDBAccessor;
        this.amqpValidationResultSender = amqpValidationResultSender;
    }

    @Override
    public void validatePermit(String transponderId) {
        amqpValidationResultSender.sendValidationResult(permitDBAccessor.validPermit(transponderId));
    }
}
