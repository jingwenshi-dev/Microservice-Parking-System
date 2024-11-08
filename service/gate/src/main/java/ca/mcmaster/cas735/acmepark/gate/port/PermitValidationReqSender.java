package ca.mcmaster.cas735.acmepark.gate.port;

public interface PermitValidationReqSender {
    void validatePermit(String transponderId);
}
