package ca.mcmaster.cas735.acmepark.permit.port;

public interface PermitValidationResultSender {
    void sendValidationResult(boolean isValid);
}
