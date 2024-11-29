package ca.mcmaster.cas735.acmepark.violation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketLookupDTO {
    private String ticketId;
    private String licensePlate;
}
