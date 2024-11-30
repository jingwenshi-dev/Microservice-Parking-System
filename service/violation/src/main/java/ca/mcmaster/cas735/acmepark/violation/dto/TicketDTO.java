package ca.mcmaster.cas735.acmepark.violation.dto;

import ca.mcmaster.cas735.acmepark.payment.business.entities.ParkingViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TicketDTO {

    private UUID ticketNum;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotNull(message = "Fine amount is required")
    private BigDecimal fineAmount;

    @NotNull(message = "Officer ID is required")
    private long officerId;

    @NotNull(message = "Lot ID is required")
    private long lotId;

    private LocalDateTime violationTime = LocalDateTime.now();

    public ParkingViolation asParkingViolation() {
        ParkingViolation violation = new ParkingViolation();
        violation.setLicensePlate(licensePlate);
        violation.setFineAmount(fineAmount);
        violation.setOfficerId(officerId);
        violation.setLotId(lotId);
        violation.setViolationTime(violationTime);
        return violation;
    }

    public TicketDTO(ParkingViolation violation) {
        this.ticketNum = violation.getViolationId();
        this.licensePlate = violation.getLicensePlate();
        this.fineAmount = violation.getFineAmount();
        this.officerId = violation.getOfficerId();
        this.lotId = violation.getLotId();
        this.violationTime = violation.getViolationTime();
    }

}
