package ca.mcmaster.cas735.acmepark.visitor_access.ports;

import ca.mcmaster.cas735.acmepark.visitor_access.dto.VisitorRequest;

public interface VisitorSender {

    void sendOpenGateEntryRequest(VisitorRequest visitorRequest);

    void sendOpenGateExitRequest(VisitorRequest exitRequest);

    void sendGateEntryResponseToVisitor(String sessionId, boolean gateOpened, String qrCode);

    void sendGateExitResponseToVisitor(String sessionId, boolean gateOpened, String qrCode);
}