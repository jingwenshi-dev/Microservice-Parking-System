package ca.mcmaster.cas735.acmepark.violation.dto;

import ca.mcmaster.cas735.acmepark.violation.business.entities.ParkingViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TicketDTO {

    private long ticketNum;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotNull(message = "Fine amount is required")
    private BigDecimal fineAmount;

    @NotNull(message = "Officer ID is required")
    private long officerId;

    @NotNull(message = "Lot ID is required")
    private long lotId;

    private LocalDateTime timestamp = LocalDateTime.now();

    public TicketDTO(ParkingViolation violation) {
        this.ticketNum = violation.getViolationId();
        this.licensePlate = violation.getLicensePlate();
        this.fineAmount = violation.getFineAmount();
        this.officerId = violation.getOfficerId();
        this.lotId = violation.getLotId();
        this.timestamp = violation.getViolationTime();
    }

}
