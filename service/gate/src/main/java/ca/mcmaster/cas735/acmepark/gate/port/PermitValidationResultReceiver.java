package ca.mcmaster.cas735.acmepark.gate.port;

public interface PermitValidationResultReceiver {
    void receiveValidationResult(boolean isValid);
}
