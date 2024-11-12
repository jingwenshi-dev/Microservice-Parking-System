package ca.mcmaster.cas735.acmepark.visitor_access.ports;

public interface GateInteractionHandler {
    void handleGateEntryResponse(String data);

    void handleGateExitResponse(String data);
}
