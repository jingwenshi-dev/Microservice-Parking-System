package ca.mcmaster.cas735.acmepark.visitor_access.ports.provided;


import ca.mcmaster.cas735.acmepark.visitor_access.dto.GateAccessRequest;

public interface VisitorSender {

    void sendOpenGateEntryRequest(String visitorRequest);

    void sendOpenGateExitRequest(String exitRequest);

    void sendEntryResponseToGate(GateAccessRequest gateAccessRequest);

    void sendGateExitResponseToVisitor(GateAccessRequest gateAccessRequest);
}