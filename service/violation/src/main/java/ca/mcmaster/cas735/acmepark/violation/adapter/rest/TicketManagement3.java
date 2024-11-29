package ca.mcmaster.cas735.acmepark.violation.adapter.rest;

import ca.mcmaster.cas735.acmepark.violation.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.violation.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.violation.port.provided.TicketManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Pay tickets")
@RequestMapping(value = "/tickets")
public class TicketManagement3 {

    private final TicketManager manager;

    @Autowired
    public TicketManagement3(TicketManager manager) {
        this.manager = manager;
    }

    @GetMapping(value = "/lookup")
    @Operation(description = "Lookup for tickets with a given ticket id and license plate")
    public TicketDTO lookup(@RequestParam long ticketNum, @RequestParam String licensePlate) throws NotFoundException {
        return manager.lookupTicket(ticketNum, licensePlate);
    }

    @PostMapping(value = "/issue")
    @Operation(description = "Issue a ticket")
    public String issue(@Valid @RequestBody TicketDTO ticket) {
        return manager.issueTicket(ticket);
    }

}
