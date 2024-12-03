package ca.mcmaster.cas735.acmepark.violation.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.violation.port.provided.TicketManager;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPTicketDeleteListener {

    private final TicketManager ticketManager;

    @Autowired
    public AMQPTicketDeleteListener(TicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    @RabbitListener(queuesToDeclare = @Queue("ticket.delete.queue"))
    public void receiveTicketDelete(String raw) {
        PaymentRequest paymentResult = translate(raw);
        ticketManager.deleteTickets(paymentResult.getLicensePlate());
    }

    private PaymentRequest translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(raw, PaymentRequest.class);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
