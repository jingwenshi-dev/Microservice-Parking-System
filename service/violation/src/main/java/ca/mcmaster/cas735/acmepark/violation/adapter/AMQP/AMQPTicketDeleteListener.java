package ca.mcmaster.cas735.acmepark.violation.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.violation.port.provided.TicketManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPTicketDeleteListener {

    private final TicketManager ticketManager;
    private final ObjectMapper mapper;

    @Autowired
    public AMQPTicketDeleteListener(TicketManager ticketManager) {
        this.ticketManager = ticketManager;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    @RabbitListener(queuesToDeclare = @Queue("ticket.delete.queue"))
    public void receiveTicketDelete(String raw) {
        PaymentRequest paymentResult = translate(raw);
        ticketManager.deleteTickets(paymentResult.getLicensePlate());
    }

    private PaymentRequest translate(String raw) {
        try {
            return mapper.readValue(raw, PaymentRequest.class);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
