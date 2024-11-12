package ca.mcmaster.cas735.acmepark.visitor_access.ports;

import ca.mcmaster.cas735.acmepark.visitor_access.domain.PaymentRequest;

public interface VisitorService {
    void handleVisitorEntry(String licensePlate);

    void handleVisitorExit(String licensePlate);

    void handleGateEntryResponse(String data);

    void handleGateExitResponse(String data);

    String translate(String licensePlate, boolean entry);
}
