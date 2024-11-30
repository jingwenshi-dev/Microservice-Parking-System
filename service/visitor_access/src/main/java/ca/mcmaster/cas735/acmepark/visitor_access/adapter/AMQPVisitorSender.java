package ca.mcmaster.cas735.acmepark.visitor_access.adapter;

import ca.mcmaster.cas735.acmepark.visitor_access.dto.GateAccessRequest;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.VisitorSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPVisitorSender implements VisitorSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPVisitorSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.custom.messaging.visitor-to-gate-entry-request-exchange}")
    private String entryRequestExchange;

    @Value("${app.custom.messaging.visitor-to-gate-exit-request-exchange}")
    private String exitRequestExchange;

    @Value("${app.custom.messaging.visitor-entry-response-exchange}")
    private String entryResponseExchange;

    @Value("${app.custom.messaging.visitor-exit-response-exchange}")
    private String exitResponseExchange;

    // 发送进入请求给gate service
    // 从监听访客请求到该方法，数据均为改变，相当于直接透传服务
    @Override
    public void sendOpenGateEntryRequest(String visitorRequest) {
        log.debug("Sending entry request message to {}: {}", entryRequestExchange, visitorRequest);
        rabbitTemplate.convertAndSend(entryRequestExchange, "*", visitorRequest);
    }

    // 发送离开请求给gate service
    // 从监听访客请求到该方法，数据均为改变，相当于直接透传服务
    @Override
    public void sendOpenGateExitRequest(String exitRequest) {
        log.debug("Sending exit request message to {}: {}", exitRequestExchange, exitRequest);
        rabbitTemplate.convertAndSend(exitRequestExchange, "*", exitRequest);
    }

    // 发送进入响应给访客
    @Override
    public void sendEntryResponseToGate(GateAccessRequest gateAccessRequest) {
        log.debug("Sending gate entry response message to {}: {}", entryResponseExchange, gateAccessRequest);
        rabbitTemplate.convertAndSend(entryResponseExchange, "*", translate(gateAccessRequest));
    }

    // 发送离开响应给访客
    @Override
    public void sendGateExitResponseToVisitor(GateAccessRequest gateAccessRequest) {
        log.debug("Sending gate exit response message to {}: {}", exitResponseExchange, gateAccessRequest);
        rabbitTemplate.convertAndSend(exitResponseExchange, "*", translate(gateAccessRequest));
    }

    private String translate(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 将对象转换为 JSON 字符串
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            // 如果转换失败，抛出运行时异常
            log.error("Object translation error:", e);
            throw new RuntimeException(e);
        }
    }
}