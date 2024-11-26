package ca.mcmaster.cas735.acmepark.payment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PaymentRequest {
    private String userType;
    private String licensePlate;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private String paymentMethod; // "credit", "debit", "voucher", "direct", "payroll"
    private BigDecimal amount;
    private String permitServiceResponseQueue;
    private BigDecimal hourlyRate;
}
