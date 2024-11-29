package ca.mcmaster.cas735.acmepark.violation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class TicketDTO {

    private String licensePlate;
    private String amount;
    private String officerId;
    private String lotId;
    private ZonedDateTime timestamp = ZonedDateTime.now();

}
