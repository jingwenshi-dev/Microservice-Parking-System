package ca.mcmaster.cas735.acmepark.violation.business;

import ca.mcmaster.cas735.acmepark.payment.business.entities.ParkingViolation;
import ca.mcmaster.cas735.acmepark.violation.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.violation.port.provided.TicketManager;
import ca.mcmaster.cas735.acmepark.violation.port.required.ParkingLotDataRepo;
import ca.mcmaster.cas735.acmepark.violation.port.required.TicketDataRepo;
import ca.mcmaster.cas735.acmepark.violation.port.required.VisitorDataRepo;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class TicketProcessor implements TicketManager {

    private final TicketDataRepo ticketDB;
    private final VisitorDataRepo visitorDB;
    private final ParkingLotDataRepo parkingLotDB;

    public TicketProcessor(TicketDataRepo ticketDB, VisitorDataRepo visitorDB, ParkingLotDataRepo parkingLotDB) {
        this.ticketDB = ticketDB;
        this.visitorDB = visitorDB;
        this.parkingLotDB = parkingLotDB;
    }

    @Override
    public TicketDTO lookupTicket(UUID ticketNum, String licensePlate) throws NotFoundException {
        return new TicketDTO(ticketDB.findByViolationIdAndLicensePlate(ticketNum, licensePlate)
                .orElseThrow(() -> new NotFoundException("Ticket number or license plate not found.")));
    }

    @Override
    public UUID issueTicket(TicketDTO ticket) throws NotFoundException {
        ParkingViolation violation = ticket.asParkingViolation();

        if (visitorDB.existsById(ticket.getLicensePlate()) && parkingLotDB.existsById(ticket.getLotId())) {
            ticketDB.saveAndFlush(violation);
            return violation.getViolationId();
        } else {
            throw new NotFoundException("License plate or parking lot id not found.");
        }
    }
}
