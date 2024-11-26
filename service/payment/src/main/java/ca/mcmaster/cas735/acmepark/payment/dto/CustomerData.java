package ca.mcmaster.cas735.acmepark.payment.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerData {

    private int userId;

    private String userType;

    private int totalFine;

    // ParkingPermits 表的字段
    private String transponderNumber;

    private String validFrom;

    private String validUntil;

    private String licensePlate;

    private String paymentMethod;

    private Integer payrollNum;

    // ParkingLots 表的字段
    private int lotId;

    private String lotName;

    private int totalSpots;

    private boolean visitorAllowed;

    private double hourlyRate;

    private String location;

    // Visitor 表的字段
    private String voucher;

    private String entryTime;

    // Voucher 表的字段
    private String validFromVoucher;

    private String validUntilVoucher;

    // ParkingViolations 表的字段
    private int violationId;

    private String violationTime;

    private double fineAmount;

    private boolean isPaid;

    private int officerId;

    // LotOccupancy 表的字段
    private String timestamp;

    private int currentOccupancy;
}
