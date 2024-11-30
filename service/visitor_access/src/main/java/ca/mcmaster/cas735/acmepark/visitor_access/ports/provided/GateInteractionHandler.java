package ca.mcmaster.cas735.acmepark.visitor_access.ports.provided;

public interface GateInteractionHandler {
    void handleGateEntryRequest(String data);

    void handleGateExitRequest(String data);
}
