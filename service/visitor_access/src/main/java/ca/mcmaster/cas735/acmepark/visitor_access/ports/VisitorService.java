package ca.mcmaster.cas735.acmepark.visitor_access.ports;

import ca.mcmaster.cas735.acmepark.visitor_access.domain.PaymentRequest;

public interface VisitorService {
    void handleVisitorEntry(String data);

    void handleVisitorExit(String data);

    void handleGateEntryResponse(String data);

    void handleGateExitResponse(String data);

}
