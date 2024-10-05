package dev.jingwenshi.permit.adapter;

import dev.jingwenshi.permit.port.PermitValidator;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPValidationRequestHandler {
    private final PermitValidator permitValidator;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPValidationRequestHandler(PermitValidator permitValidator, RabbitTemplate rabbitTemplate) {
        this.permitValidator = permitValidator;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queuesToDeclare = @Queue("permit.validation.queue"))
    public void validatePermit(String transponderId, Message message) {
        String replyTo = message.getMessageProperties().getReplyTo();
        if (replyTo != null) {
            rabbitTemplate.convertAndSend(replyTo, permitValidator.validatePermit(transponderId));
        }
    }
}
