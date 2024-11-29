package ca.mcmaster.cas735.acmepark.violation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketLookupDTO {

    @NotNull(message = "Ticket number is required")
    private long ticketNum;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

}
