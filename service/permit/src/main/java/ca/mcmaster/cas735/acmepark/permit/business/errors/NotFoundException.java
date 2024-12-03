package ca.mcmaster.cas735.acmepark.permit.business.errors;

public class NotFoundException extends RuntimeException  {
    public NotFoundException(String message) {
        super(message);
    }
}
