package ca.mcmaster.cas735.acmepark.payment.adapter;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.TicketDeleteSender;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPTicketDeleteSender implements TicketDeleteSender {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.ticket-delete-queue}")
    private String ticketDeleteQueue;

    public AMQPTicketDeleteSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendTicketDelete(PaymentRequest paymentRequest) {
        try {
            String message =translate(paymentRequest);
            rabbitTemplate.convertAndSend(ticketDeleteQueue, message);
            log.debug("Ticket delete message sent to queue: " + ticketDeleteQueue);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send ticket delete message", e);
        }
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
