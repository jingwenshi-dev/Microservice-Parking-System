package dev.jingwenshi.permit.port;

public interface PermitValidator {
    boolean validatePermit(String transponderId);
}
