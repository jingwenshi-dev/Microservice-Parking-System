package ca.mcmaster.cas735.acmepark.payment.adapter;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentSender;
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
public class AMQPPaymentSender implements PaymentSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.payment-response-permit-exchange}")
    private String paymentResultPermitExchange;

    @Value("${app.custom.messaging.payment-response-visitor-exchange}")
    private String paymentResultVisitorExchange;

    @Autowired
    public AMQPPaymentSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendPaymentResultToPermit(PaymentRequest paymentRequest) {
        log.debug("Sending payment result to Permit service: {}", paymentRequest);
        String message = translate(paymentRequest);
        rabbitTemplate.convertAndSend(paymentResultPermitExchange, "*", message);
    }

    @Override
    public void sendPaymentResultToVisitor(PaymentRequest paymentRequest) {
        log.debug("Sending payment result to Visitor service: {}", paymentRequest);
        String message = translate(paymentRequest);
        rabbitTemplate.convertAndSend(paymentResultVisitorExchange, "*", message);
    }

    private String translate(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Object translation error:", e);
            throw new RuntimeException(e);
        }
    }
}