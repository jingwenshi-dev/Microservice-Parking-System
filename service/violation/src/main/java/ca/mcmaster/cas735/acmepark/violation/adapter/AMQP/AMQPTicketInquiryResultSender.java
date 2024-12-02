package ca.mcmaster.cas735.acmepark.violation.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.violation.business.entities.ParkingViolation;
import ca.mcmaster.cas735.acmepark.violation.port.TicketInquiryResultSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AMQPTicketInquiryResultSender implements TicketInquiryResultSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public AMQPTicketInquiryResultSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public void sendTicketInquiryResult(List<ParkingViolation> tickets) {
        System.out.println(translate(tickets));
        rabbitTemplate.convertAndSend("ticket-inquiry-result", translate(tickets));
    }

    private String translate(List<ParkingViolation> tickets) {
        try {
            return mapper.writeValueAsString(tickets);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}