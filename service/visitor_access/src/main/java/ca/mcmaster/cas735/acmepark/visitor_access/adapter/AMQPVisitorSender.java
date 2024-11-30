package ca.mcmaster.cas735.acmepark.visitor_access.adapter;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
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

//    @Value("${app.custom.messaging.visitor-to-gate-entry-request-exchange}")
//    private String entryRequestExchange;

    @Value("${app.custom.messaging.visitor-to-gate-exit-request-exchange}")
    private String exitRequestExchange;

    @Value("${app.custom.messaging.visitor-entry-response-exchange}")
    private String entryResponseExchange;

//    @Value("${app.custom.messaging.visitor-exit-response-exchange}")
//    private String exitResponseExchange;

    @Value("${app.custom.messaging.payment-request-exchange}")
    private String paymentRequestExchange;

//    // 发送进入请求给gate service
//    // 从监听访客请求到该方法，数据均为改变，相当于直接透传服务
//    @Override
//    public void sendOpenGateEntryRequest(String visitorRequest) {
//        log.debug("Sending entry request message to {}: {}", entryRequestExchange, visitorRequest);
//        rabbitTemplate.convertAndSend(entryRequestExchange, "*", visitorRequest);
//    }

    // 发送离开请求给gate service
    // 从监听访客请求到该方法，数据均为改变，相当于直接透传服务
    @Override
    public void sendExitRequestToGate(PaymentRequest paymentRequest) {

        log.debug("Sending exit request message to {}: {}", exitRequestExchange, paymentRequest);
        rabbitTemplate.convertAndSend(exitRequestExchange, "*", paymentRequest);
    }


    // 发送进入响应给访客
    @Override
    public void sendEntryResponseToGate(GateCtrlDTO gateCtrlDTO) {
        log.debug("Sending gate entry response message to {}: {}", entryResponseExchange, gateCtrlDTO);
        rabbitTemplate.convertAndSend(entryResponseExchange, "*", translate(gateCtrlDTO));
    }

//    // 发送离开响应给访客
//    @Override
//    public void sendGateExitResponseToVisitor(GateCtrlDTO gateCtrlDTO) {
//        log.debug("Sending gate exit response message to {}: {}", exitResponseExchange, gateCtrlDTO);
//        rabbitTemplate.convertAndSend(exitResponseExchange, "*", translate(gateCtrlDTO));
//    }

    // 请求交易，进行扣费
    @Override
    public void sendExitRequestToPayment(PaymentRequest paymentRequest) {
        log.debug("Sending exit  response message to {}: {}", paymentRequestExchange, paymentRequest);
        rabbitTemplate.convertAndSend(paymentRequestExchange, "*", translate(paymentRequest));
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