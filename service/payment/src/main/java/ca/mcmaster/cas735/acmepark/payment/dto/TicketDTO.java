package ca.mcmaster.cas735.acmepark.payment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TicketDTO {

    private UUID ticketNum;

    private String licensePlate;
    private BigDecimal fineAmount;
    private long officerId;
    private long lotId;
    private LocalDateTime violationTime = LocalDateTime.now();

}
