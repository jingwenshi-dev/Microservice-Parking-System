package ca.mcmaster.cas735.acmepark.visitor_access.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// TODO:这个地方是否允许重复创建一个类呢.
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

    //支付结果
    private boolean result;

}
