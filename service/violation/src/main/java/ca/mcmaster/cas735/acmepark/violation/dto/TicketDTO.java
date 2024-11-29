package ca.mcmaster.cas735.acmepark.violation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TicketDTO {

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotNull(message = "Fine amount is required")
    private BigDecimal fineAmount;

    @NotNull(message = "Officer ID is required")
    private long officerId;

    @NotNull(message = "Lot ID is required")
    private long lotId;

    private LocalDateTime timestamp = LocalDateTime.now();

}
