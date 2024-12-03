package ca.mcmaster.cas735.acmepark.visitor_access.adapter;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.VisitorSender;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    @Value("${app.custom.messaging.visitor-to-gate-exchange}")
    private String visitorToGateExchange;

    @Value("${app.custom.messaging.payment-request-exchange}")
    private String paymentRequestExchange;


    // 发送进入
    @Override
    public void sendEntryResponseToGate(GateCtrlDTO gateCtrlDTO) {
        log.debug("Sending gate entry response message to {}: {}", visitorToGateExchange, gateCtrlDTO);
        rabbitTemplate.convertAndSend(visitorToGateExchange, "*", translate(gateCtrlDTO));
    }

    // 发送离开请求给gate service
    // 从监听访客请求到该方法，数据均为改变，相当于直接透传服务
    @Override
    public void sendExitResponseToGate(GateCtrlDTO gateCtrlDTO) {
        log.debug("Sending exit request message to {}: {}", visitorToGateExchange, gateCtrlDTO);
        rabbitTemplate.convertAndSend(visitorToGateExchange, "*", translate(gateCtrlDTO));
    }

    // 请求交易，进行扣费
    @Override
    public void sendExitRequestToPayment(PaymentRequest paymentRequest) {
        log.debug("Sending exit  response message to {}: {}", paymentRequestExchange, paymentRequest);
        rabbitTemplate.convertAndSend(paymentRequestExchange, "*", translate(paymentRequest));
    }

    private String translate(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Register the JavaTimeModule to handle LocalDateTime
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 将对象转换为 JSON 字符串
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            // 如果转换失败，抛出运行时异常
            log.error("Object translation error:", e);
            throw new RuntimeException(e);
        }
    }
}