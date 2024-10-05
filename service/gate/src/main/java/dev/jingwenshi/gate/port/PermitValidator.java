package dev.jingwenshi.gate.port;

public interface PermitValidator {
    boolean validatePermit(String transponderId);
}
