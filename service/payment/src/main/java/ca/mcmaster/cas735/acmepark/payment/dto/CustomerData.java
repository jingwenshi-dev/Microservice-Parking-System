package ca.mcmaster.cas735.acmepark.payment.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerData {

    private int userId;

    private String userType;

    private int totalFine;

    private String transponderNumber;

    private String validFrom;

    private String validUntil;

    private String licensePlate;

    private String paymentMethod;

    private Integer payrollNum;

    private int lotId;

    private String lotName;

    private int totalSpots;

    private boolean visitorAllowed;

    private double hourlyRate;

    private String location;

    private String voucher;

    private String entryTime;

    private String validFromVoucher;

    private String validUntilVoucher;

    private int violationId;

    private String violationTime;

    private double fineAmount;

    private boolean isPaid;

    private int officerId;

    private String timestamp;

    private int currentOccupancy;
}
