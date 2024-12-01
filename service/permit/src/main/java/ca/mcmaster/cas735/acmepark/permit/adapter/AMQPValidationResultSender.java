package ca.mcmaster.cas735.acmepark.permit.adapter;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationResponseDTO;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidationResultSender;
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

    @Value("${spring.rabbitmq.app.custom.messaging.outbound-exchange-topic}")
    private String outboundExchange;


    @Override
    public void sendValidationResult(PermitValidationResponseDTO response) {
        logger.info("Sending validation result...");
        logger.info("Transponder ID: {}", response.getTransponderId());
        logger.info("Is Valid: {}", response.isValid());



        rabbitTemplate.convertAndSend(
            outboundExchange,
            "permit.validation.result.queue",
                response
        );
    }
}
