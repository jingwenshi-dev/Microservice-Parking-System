package ca.mcmaster.cas735.acmepark.gate.adapter;

import ca.mcmaster.cas735.acmepark.gate.dto.TransponderDTO;
import ca.mcmaster.cas735.acmepark.gate.port.ValidationReqSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPValidationReqSender implements ValidationReqSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPValidationReqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(TransponderDTO transponder) {
        if (transponder.getTransponderId() == null) {
            rabbitTemplate.convertAndSend(
                    "",
                    "visitor.validation.queue",
                    translate(transponder)
            );
        }
        else {
            rabbitTemplate.convertAndSend(
                    "",
                    "permit.validation.queue",
                    translate(transponder)
            );
        }
    }

    private String translate(TransponderDTO transponder) {
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(transponder);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}