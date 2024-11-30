package ca.mcmaster.cas735.acmepark.visitor_access.ports.provided;


public interface VisitorRequestHandler {
    void handleVisitorEntry(String data);

    void handleVisitorExit(String data);
}
