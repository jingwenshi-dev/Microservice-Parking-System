package ca.mcmaster.cas735.acmepark.payment.ports;

import ca.mcmaster.cas735.acmepark.payment.dto.TicketDTO;

import java.util.List;

public interface ViolationInquirySender {
    List<TicketDTO> sendInquiry(String licensePlate);
}
