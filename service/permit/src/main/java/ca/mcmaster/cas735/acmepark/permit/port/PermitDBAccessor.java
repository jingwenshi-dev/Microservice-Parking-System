package ca.mcmaster.cas735.acmepark.permit.port;

public interface PermitDBAccessor {
    boolean validPermit(String transponderId);
}
