package ca.mcmaster.cas735.acmepark.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    private String userType;

    private String licensePlate;

    private String transponderNumber;

    private LocalDate validFrom;

    private LocalDate validUntil;

    private String paymentMethod;

    private String payrollNum;

    private BigDecimal amount;

    private LocalDateTime entryTime;

    private BigDecimal hourlyRate;

}
