package dev.jingwenshi.permit.port;

public interface PermitValidationResultSender {
    void sendValidationResult(boolean isValid);
}
