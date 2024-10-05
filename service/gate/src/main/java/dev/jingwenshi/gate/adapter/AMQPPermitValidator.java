package dev.jingwenshi.gate.adapter;

import dev.jingwenshi.gate.port.PermitValidator;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPPermitValidator implements PermitValidator {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPPermitValidator(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public boolean validatePermit(String transponderId) {
        Boolean result = (Boolean) rabbitTemplate.convertSendAndReceive(
                "",
                "permit.validation.queue",
                transponderId
        );
        return result != null && result;
    }
}