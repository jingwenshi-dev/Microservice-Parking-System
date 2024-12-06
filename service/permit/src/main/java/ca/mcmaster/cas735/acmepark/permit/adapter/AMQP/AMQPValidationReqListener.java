package ca.mcmaster.cas735.acmepark.permit.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationRequestDTO;
import ca.mcmaster.cas735.acmepark.permit.business.GateInteractionService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;

@Service
public class AMQPValidationReqListener {
    private final GateInteractionService gateInteractionService;

    @Autowired
    public AMQPValidationReqListener(GateInteractionService gateInteractionService) {
        this.gateInteractionService = gateInteractionService;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "permit.validation.queue", durable = "true"), // Declare the queue
            exchange = @Exchange(value = "${app.custom.messaging.gate-to-permit-exchange}", ignoreDeclarationExceptions = "true", type = "topic"), // Declare the exchange
            key = "*")) // Specify the routing key

    public void validatePermit(String data) {
        System.out.println("Received Plain Text Message: " + data);
        PermitValidationRequestDTO request = translate(data);
        System.out.println("Translated Message: " + request);
        try {
            gateInteractionService.validatePermit(request);
        } catch (Exception e) {
            System.err.println("Failed to process payment success event: " + e.getMessage());
        }
    }

    private PermitValidationRequestDTO translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(raw, PermitValidationRequestDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

