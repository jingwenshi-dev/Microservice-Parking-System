package ca.mcmaster.cas735.acmepark.gate.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.gate.port.GateController;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AMQPGateController implements GateController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPGateController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.custom.messaging.gate.exchange}") private String gateExchange;
    @Value("${app.custom.messaging.gate.routing-key}") private String gateRoutingKey;

    @Override
    public void gateControl(GateCtrlDTO gateCtrl) {
        String routingKey = String.format(gateRoutingKey, gateCtrl.getGateId());
        rabbitTemplate.convertAndSend(gateExchange, routingKey, gateCtrl.getIsValid());
    }
}
