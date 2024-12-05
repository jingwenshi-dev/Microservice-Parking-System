package ca.mcmaster.cas735.acmepark.payment.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PermitPaymentListener {

    // For payment logic processing
    private final PaymentHandler paymentProcessor;

    @Autowired
    public PermitPaymentListener(PaymentHandler paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "permit.payment.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.payment-request-exchange}", ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))
    public void listen(String data) {
        log.debug("Received payment request: {}", data);
        PaymentRequest paymentRequest = translate(data);

        // Processing of payment requests
        paymentProcessor.handlePayment(paymentRequest);
    }

    private PaymentRequest translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();

        // Register the JavaTimeModule to handle LocalDateTime
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(raw, PaymentRequest.class);
        } catch (Exception e) {
            log.error("Failed to parse payment request: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}