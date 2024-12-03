package ca.mcmaster.cas735.acmepark.violation.business;

import ca.mcmaster.cas735.acmepark.violation.business.entities.ParkingViolation;
import ca.mcmaster.cas735.acmepark.violation.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.violation.port.provided.TicketManager;
import ca.mcmaster.cas735.acmepark.violation.port.required.TicketDataRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketProcessor implements TicketManager {

    private final TicketDataRepo ticketDB;

    @Autowired
    public TicketProcessor(TicketDataRepo ticketDB) {
        this.ticketDB = ticketDB;
    }

    @Override
    @Transactional
    public void deleteTickets(String licensePLate) {
        ticketDB.deleteAllByLicensePlate(licensePLate);
    }

    @Override
    public List<TicketDTO> lookupTicket(String licensePlate) throws NotFoundException {
        List<ParkingViolation> violations = ticketDB.findAllByLicensePlate(licensePlate);

        if (violations.isEmpty()) {
            throw new NotFoundException("Ticket number or license plate not found.");
        }

        return violations.stream()
                .map(TicketDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public UUID issueTicket(TicketDTO ticket) {
        ParkingViolation savedViolation = ticketDB.saveAndFlush(ticket.asParkingViolation());
        return savedViolation.getViolationId();
    }
}
