package ca.mcmaster.cas735.acmepark.permit.adapter;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationRequestDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitValidationResponseDTO;
import ca.mcmaster.cas735.acmepark.permit.business.GateInteractionService;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidationResultSender;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidator;
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


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "permit.validation.queue", durable = "true"), // Declare the queue
            exchange = @Exchange(value = "${app.custom.messaging.gate-to-permit-exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"), // Declare the exchange
            key = "*")) // Specify the routing key

    public void validatePermit(PermitValidationRequestDTO request) {
        gateInteractionService.validatePermit(request);
    }
}
