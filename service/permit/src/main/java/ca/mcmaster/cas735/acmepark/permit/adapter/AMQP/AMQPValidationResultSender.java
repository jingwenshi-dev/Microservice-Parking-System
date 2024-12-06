package ca.mcmaster.cas735.acmepark.permit.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.permit.port.PermitValidationResultSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AMQPValidationResultSender implements PermitValidationResultSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPValidationResultSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.custom.messaging.permit-to-gate-exchange}")
    private String permitToGateExchange;

    @Override
    public void sendValidationResult(GateCtrlDTO response) {
        rabbitTemplate.convertAndSend(permitToGateExchange, "*", response);
    }

}
