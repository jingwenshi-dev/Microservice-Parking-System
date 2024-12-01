package ca.mcmaster.cas735.acmepark.violation.business;

import ca.mcmaster.cas735.acmepark.violation.adapter.AMQP.AMQPTicketInquiryResultSender;
import ca.mcmaster.cas735.acmepark.violation.business.entities.ParkingViolation;
import ca.mcmaster.cas735.acmepark.violation.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.violation.port.provided.TicketManager;
import ca.mcmaster.cas735.acmepark.violation.port.required.TicketDataRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketProcessor implements TicketManager {

    private final TicketDataRepo ticketDB;
    private final AMQPTicketInquiryResultSender amqpTicketInquiryResultSender;

    public TicketProcessor(TicketDataRepo ticketDB, AMQPTicketInquiryResultSender amqpTicketInquiryResultSender) {
        this.ticketDB = ticketDB;
        this.amqpTicketInquiryResultSender = amqpTicketInquiryResultSender;
    }

    @Override
    public void ticketsInquiry(String licensePlate) {
        Optional<List<ParkingViolation>> violations = ticketDB.findByLicensePlate(licensePlate);
        amqpTicketInquiryResultSender.sendTicketInquiryResult(violations);
    }

    @Override
    public TicketDTO lookupTicket(UUID ticketNum, String licensePlate) throws NotFoundException {
        return new TicketDTO(ticketDB.findByViolationIdAndLicensePlate(ticketNum, licensePlate)
                .orElseThrow(() -> new NotFoundException("Ticket number or license plate not found.")));
    }

    @Override
    public UUID issueTicket(TicketDTO ticket) {
        ParkingViolation savedViolation = ticketDB.saveAndFlush(ticket.asParkingViolation());
        return savedViolation.getViolationId();
    }
}
