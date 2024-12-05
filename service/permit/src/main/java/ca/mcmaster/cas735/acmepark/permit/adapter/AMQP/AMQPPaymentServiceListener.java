package ca.mcmaster.cas735.acmepark.permit.adapter.AMQP;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.business.PermitProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPPaymentServiceListener{
    private final PermitProcessor permitApplicationService;
    @Autowired
    public AMQPPaymentServiceListener(PermitProcessor permitApplicationService) {
        this.permitApplicationService = permitApplicationService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "payment.success.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.payment-response-permit-exchange}", ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))

    public void handlePaymentSuccess(String data) {
        System.out.println("Received Plain Text Message: " + data);
        PermitCreatedDTO event = translate(data);
        try {
            // Notify the business service to process the payment success event
            permitApplicationService.processPaymentSuccess(event);

        } catch (Exception e) {
            System.err.println("Failed to process payment success event: " + e.getMessage());
        }
    }

    private PermitCreatedDTO translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(raw, PermitCreatedDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
