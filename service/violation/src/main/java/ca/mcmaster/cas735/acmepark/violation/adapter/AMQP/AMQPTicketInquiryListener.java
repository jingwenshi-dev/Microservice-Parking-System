package ca.mcmaster.cas735.acmepark.violation.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.violation.port.provided.TicketManager;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPTicketInquiryListener {

    private final TicketManager ticketManager;

    @Autowired
    public AMQPTicketInquiryListener(TicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    @RabbitListener(queues = "ticket-inquiry")
    public void receiveTicketInquiry(String licensePlate) {
        ticketManager.ticketsInquiry(licensePlate);
    }

}
