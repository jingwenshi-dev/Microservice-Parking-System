package ca.mcmaster.cas735.acmepark.violation.port.provided;

import ca.mcmaster.cas735.acmepark.violation.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketLookupDTO;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketPaymentDTO;


public interface TicketManager {
    TicketDTO lookupTicket(TicketLookupDTO ticket);
    String payTicket(TicketPaymentDTO ticket);
    String issueTicket(TicketDTO ticket);
}
