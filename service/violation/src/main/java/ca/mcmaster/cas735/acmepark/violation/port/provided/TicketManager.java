package ca.mcmaster.cas735.acmepark.violation.port.provided;

import ca.mcmaster.cas735.acmepark.violation.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketDTO;


public interface TicketManager {
    TicketDTO lookupTicket(long ticketNum, String licensePlate) throws NotFoundException;
    String issueTicket(TicketDTO ticket);
}
