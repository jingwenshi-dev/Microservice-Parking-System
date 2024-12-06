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



    @Override
    public void sendEntryResponseToGate(GateCtrlDTO gateCtrlDTO) {
        log.debug("Sending gate entry response message to {}: {}", visitorToGateExchange, gateCtrlDTO);
        rabbitTemplate.convertAndSend(visitorToGateExchange, "*", translate(gateCtrlDTO));
    }

    // Send a leave request to the gate service
    @Override
    public void sendExitResponseToGate(GateCtrlDTO gateCtrlDTO) {
        log.debug("Sending exit request message to {}: {}", visitorToGateExchange, gateCtrlDTO);
        rabbitTemplate.convertAndSend(visitorToGateExchange, "*", translate(gateCtrlDTO));
    }

    // Request a transaction for chargeback
    @Override
    public void sendExitRequestToPayment(PaymentRequest paymentRequest) {
        log.debug("Sending exit  response message to {}: {}", paymentRequestExchange, paymentRequest);
        rabbitTemplate.convertAndSend(paymentRequestExchange, "*", translate(paymentRequest));
    }

    private String translate(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Object translation error:", e);
            throw new RuntimeException(e);
        }
    }
}