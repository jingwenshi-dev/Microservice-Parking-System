package ca.mcmaster.cas735.acmepark.permit.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationResponseDTO;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidationResultSender;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AMQPValidationResultSender implements PermitValidationResultSender {

    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(AMQPValidationResultSender.class);

    @Autowired
    public AMQPValidationResultSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.custom.messaging.permit-to-gate-exchange}")
    private String permitToGateExchange;

    @Override
    public void sendValidationResult(PermitValidationResponseDTO response) {
        logger.info("Sending validation result...");
        logger.info("Transponder ID: {}", response.getGateId());
        logger.info("Is Valid: {}", response.getIsValid());

        String data = translate(response);

        rabbitTemplate.convertAndSend(
                permitToGateExchange,
                "*",
                data
        );
    }

    private String translate(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        // Register the JavaTimeModule to handle LocalDateTime
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
