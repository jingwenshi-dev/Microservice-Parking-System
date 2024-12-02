package ca.mcmaster.cas735.acmepark.violation.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.violation.port.provided.TicketManager;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @RabbitListener(queues = "ticket-delete")
    public void receiveTicketDelete(String raw) {
        PaymentRequest paymentResult = translate(raw);
        ticketManager.deleteTickets(paymentResult.getLicensePlate());
    }

    private PaymentRequest translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, PaymentRequest.class);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
