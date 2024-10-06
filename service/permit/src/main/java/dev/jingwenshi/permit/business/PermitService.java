package dev.jingwenshi.permit.business;

import dev.jingwenshi.permit.adapter.AMQPValidationResultSender;
import dev.jingwenshi.permit.port.PermitDBAccessor;
import dev.jingwenshi.permit.port.PermitValidator;
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
