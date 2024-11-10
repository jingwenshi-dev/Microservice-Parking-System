package ca.mcmaster.cas735.acmepark.visitor_access.adapter;

import ca.mcmaster.cas735.acmepark.visitor_access.ports.GateClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VisitorAMQPClient implements GateClient {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public VisitorAMQPClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // 发送开门请求到 RabbitMQ
    @Override
    public void sendOpenGateRequest(String gateRequest) {
        rabbitTemplate.convertAndSend("visitorQueue", gateRequest);
    }
}