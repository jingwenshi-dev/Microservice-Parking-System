package dev.jingwenshi.gate.adapter;

import dev.jingwenshi.gate.port.PermitValidationReqSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPValidationReqSender implements PermitValidationReqSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPValidationReqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void validatePermit(String transponderId) {
        rabbitTemplate.convertAndSend(
                "",
                "permit.validation.queue",
                transponderId
        );
    }
}