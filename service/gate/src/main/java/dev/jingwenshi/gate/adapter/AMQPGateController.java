package dev.jingwenshi.gate.adapter;

import dev.jingwenshi.gate.port.GateController;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPGateController implements GateController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPGateController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void gateControl(boolean open) {
        rabbitTemplate.convertAndSend("", "gate.ctrl", String.valueOf(open));
    }
}
