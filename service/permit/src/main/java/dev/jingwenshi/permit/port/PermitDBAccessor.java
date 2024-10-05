package dev.jingwenshi.permit.port;

public interface PermitDBAccessor {
    boolean validPermit(String transponderId);
}
