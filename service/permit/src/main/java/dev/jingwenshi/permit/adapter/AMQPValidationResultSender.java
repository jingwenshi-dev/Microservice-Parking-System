package dev.jingwenshi.permit.adapter;

import dev.jingwenshi.permit.port.PermitValidationResultSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPValidationResultSender implements PermitValidationResultSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPValidationResultSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendValidationResult(boolean isValid) {

        String result = isValid ? "valid" : "invalid";

        rabbitTemplate.convertAndSend(
            "",
            "permit.validation.result.queue",
                result
        );
    }
}
