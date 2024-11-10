package ca.mcmaster.cas735.acmepark.visitor_access.ports;

import ca.mcmaster.cas735.acmepark.visitor_access.domain.PaymentRequest;

public interface VisitorService {
    void handleVisitorEntry(String qrCode);

    void handleVisitorExit(String qrCode);

    void processVisitorPayment(String qrCode, boolean paymentStatus);

    String translate(String raw);

    PaymentRequest translatePayment(String raw);
}