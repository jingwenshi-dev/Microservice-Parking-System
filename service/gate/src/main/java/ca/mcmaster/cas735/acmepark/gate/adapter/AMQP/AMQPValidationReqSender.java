package ca.mcmaster.cas735.acmepark.gate.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.gate.dto.TransponderDTO;
import ca.mcmaster.cas735.acmepark.gate.port.ValidationReqSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AMQPValidationReqSender implements ValidationReqSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPValidationReqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.custom.messaging.visitor.exchange.entry-request}") private String visitorExchange;
    // TODO: Update permit exchange.
    @Value("${app.custom.messaging.visitor.exchange.entry-request}") private String permitExchange;

    @Override
    public void send(TransponderDTO transponder) {
        if (transponder.getTransponderId() == null) {
            rabbitTemplate.convertAndSend(
                    visitorExchange,
                    "*",
                    translate(transponder)
            );
        }
        else {
            rabbitTemplate.convertAndSend(
                    permitExchange,
                    "*",
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