package dev.jingwenshi.permit.adapter;

import dev.jingwenshi.permit.port.PermitValidator;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPValidationReqListener {
    private final PermitValidator permitValidator;

    @Autowired
    public AMQPValidationReqListener(PermitValidator permitValidator) {
        this.permitValidator = permitValidator;
    }

    @RabbitListener(queuesToDeclare = @Queue("permit.validation.queue"))
    public void validatePermit(String transponderId) {
        permitValidator.validatePermit(transponderId);
    }
}
