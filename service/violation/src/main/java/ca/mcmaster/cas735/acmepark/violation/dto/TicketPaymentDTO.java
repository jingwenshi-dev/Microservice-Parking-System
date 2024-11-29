package ca.mcmaster.cas735.acmepark.violation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketPaymentDTO {
    private String ticketNum;
    private String licensePlate;
    private String amount;
}
