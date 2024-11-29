package ca.mcmaster.cas735.acmepark.violation.business;

import ca.mcmaster.cas735.acmepark.violation.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.violation.port.provided.TicketManager;
import ca.mcmaster.cas735.acmepark.violation.port.required.TicketDataRepo;
import org.springframework.stereotype.Service;

@Service
public class TicketProcessor implements TicketManager {

    private final TicketDataRepo db;

    public TicketProcessor(TicketDataRepo db) {
        this.db = db;
    }

    @Override
    public TicketDTO lookupTicket(long ticketNum, String licensePlate) throws NotFoundException {
        return new TicketDTO(db.findByViolationIdAndLicensePlate(ticketNum, licensePlate)
                .orElseThrow(() -> new NotFoundException(ticketNum, licensePlate)));
    }

    @Override
    public String issueTicket(TicketDTO ticket) {
        return "";
    }
}
