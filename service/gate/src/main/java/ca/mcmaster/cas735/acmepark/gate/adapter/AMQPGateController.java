package ca.mcmaster.cas735.acmepark.gate.adapter;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.gate.port.GateController;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class AMQPGateController implements GateController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPGateController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public Queue gateCtrlQueue() {
        return new Queue("gate.ctrl", true);
    }

    // TODO: Send gate ctrl msg to corresponding gateId queue and exchange
    @Override
    public void gateControl(GateCtrlDTO gateCtrl) {
        rabbitTemplate.convertAndSend("", "gate.ctrl", gateCtrl.getIsValid());
    }
}
