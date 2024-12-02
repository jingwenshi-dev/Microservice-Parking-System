package ca.mcmaster.cas735.acmepark.violation.port.provided;

import ca.mcmaster.cas735.acmepark.violation.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketDTO;

import java.util.UUID;


public interface TicketManager {
    void ticketsInquiry(String licensePlate);
    void deleteTickets(String licensePLate);
    TicketDTO lookupTicket(UUID ticketNum, String licensePlate) throws NotFoundException;
    UUID issueTicket(TicketDTO ticket) throws NotFoundException;
}
