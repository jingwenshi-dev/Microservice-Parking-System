package ca.mcmaster.cas735.acmepark.gate.adapter;

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

    @Override
    public void gateControl(boolean open) {
        rabbitTemplate.convertAndSend("", "gate.ctrl", String.valueOf(open));
    }
}
