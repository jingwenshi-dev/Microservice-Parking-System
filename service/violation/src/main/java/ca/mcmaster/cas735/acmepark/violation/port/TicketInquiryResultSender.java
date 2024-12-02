package ca.mcmaster.cas735.acmepark.violation.port;

import ca.mcmaster.cas735.acmepark.violation.business.entities.ParkingViolation;

import java.util.List;

public interface TicketInquiryResultSender {
    void sendTicketInquiryResult(List<ParkingViolation> violations);
}
