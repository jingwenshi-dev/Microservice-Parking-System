package ca.mcmaster.cas735.acmepark.violation.business;

import ca.mcmaster.cas735.acmepark.violation.business.entities.ParkingViolation;
import ca.mcmaster.cas735.acmepark.violation.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.violation.port.provided.TicketManager;
import ca.mcmaster.cas735.acmepark.violation.port.required.TicketDataRepo;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class TicketProcessor implements TicketManager {

    private final TicketDataRepo ticketDB;

    public TicketProcessor(TicketDataRepo ticketDB) {
        this.ticketDB = ticketDB;
    }

    @Override
    public TicketDTO lookupTicket(UUID ticketNum, String licensePlate) throws NotFoundException {
        return new TicketDTO(ticketDB.findByViolationIdAndLicensePlate(ticketNum, licensePlate)
                .orElseThrow(() -> new NotFoundException("Ticket number or license plate not found.")));
    }

    @Override
    public UUID issueTicket(TicketDTO ticket) throws NotFoundException {
        ParkingViolation savedViolation = ticketDB.saveAndFlush(ticket.asParkingViolation());
        return savedViolation.getViolationId();
    }
}
