package ca.mcmaster.cas735.acmepark.visitor_access.adapter;

import ca.mcmaster.cas735.acmepark.visitor_access.dto.VisitorRequest;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.VisitorSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VisitorAMQPSender implements VisitorSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public VisitorAMQPSender(RabbitTemplate rabbitTemplate) {
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

    // 发送进入请求到 RabbitMQ
    @Override
    public void sendOpenGateEntryRequest(VisitorRequest visitorRequest) {
        log.debug("Sending entry request message to {}: {}", entryRequestExchange, visitorRequest);
        rabbitTemplate.convertAndSend(entryRequestExchange, "*", translate(visitorRequest));
    }

    // 发送离开请求到 RabbitMQ
    @Override
    public void sendOpenGateExitRequest(VisitorRequest exitRequest) {
        log.debug("Sending exit request message to {}: {}", exitRequestExchange, exitRequest);
        rabbitTemplate.convertAndSend(exitRequestExchange, "*", translate(exitRequest));
    }

    // 发送进入响应给访客
    @Override
    public void sendGateEntryResponseToVisitor(String sessionId, boolean gateOpened, String qrCode) {
//        GateResponse response = new GateResponse(sessionId, gateOpened, qrCode);
//        log.debug("Sending gate entry response message to {}: {}", entryResponseExchange, response);
//        rabbitTemplate.convertAndSend(entryResponseExchange, "*", translate(response));
    }

    // 发送离开响应给访客
    @Override
    public void sendGateExitResponseToVisitor(String sessionId, boolean gateOpened, String qrCode) {
//        GateResponse response = new GateResponse(sessionId, gateOpened, qrCode);
//        log.debug("Sending gate exit response message to {}: {}", exitResponseExchange, response);
//        rabbitTemplate.convertAndSend(exitResponseExchange, "*", translate(response));
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