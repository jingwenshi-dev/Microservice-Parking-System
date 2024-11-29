package ca.mcmaster.cas735.acmepark.violation.port.provided;

import ca.mcmaster.cas735.acmepark.violation.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketLookupDTO;


public interface TicketManager {
    TicketDTO lookupTicket(TicketLookupDTO ticket);
    String payTicket(String ticketId, String licensePlate);
    String issueTicket(TicketDTO ticket);
}
