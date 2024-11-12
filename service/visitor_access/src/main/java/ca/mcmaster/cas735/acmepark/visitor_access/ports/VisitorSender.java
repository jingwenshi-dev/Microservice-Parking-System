package ca.mcmaster.cas735.acmepark.visitor_access.ports;


import ca.mcmaster.cas735.acmepark.visitor_access.dto.GateAccessRequest;

public interface VisitorSender {

    void sendOpenGateEntryRequest(String visitorRequest);

    void sendOpenGateExitRequest(String exitRequest);

    void sendGateEntryResponseToVisitor(GateAccessRequest gateAccessRequest);

    void sendGateExitResponseToVisitor(GateAccessRequest gateAccessRequest);
}