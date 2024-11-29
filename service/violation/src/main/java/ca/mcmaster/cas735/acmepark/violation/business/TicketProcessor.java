package ca.mcmaster.cas735.acmepark.violation.business;

import ca.mcmaster.cas735.acmepark.violation.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketLookupDTO;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketPaymentDTO;
import ca.mcmaster.cas735.acmepark.violation.port.provided.TicketManager;
import org.springframework.stereotype.Service;

@Service
public class TicketProcessor implements TicketManager {

    @Override
    public TicketDTO lookupTicket(TicketLookupDTO ticket) {
        return null;
    }

    @Override
    public String payTicket(TicketPaymentDTO ticket) {
        return "";
    }

    @Override
    public String issueTicket(TicketDTO ticket) {
        return "";
    }
}
