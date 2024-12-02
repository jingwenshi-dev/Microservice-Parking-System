package ca.mcmaster.cas735.acmepark.violation.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.violation.business.entities.ParkingViolation;
import ca.mcmaster.cas735.acmepark.violation.port.TicketInquiryResultSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AMQPTicketInquiryResultSender implements TicketInquiryResultSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPTicketInquiryResultSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendTicketInquiryResult(List<ParkingViolation> tickets) {
        rabbitTemplate.convertAndSend("ticket-inquiry-result", translate(tickets));
    }

    private String translate(List<ParkingViolation> tickets) {
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(tickets);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
