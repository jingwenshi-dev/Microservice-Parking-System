package ca.mcmaster.cas735.acmepark.permit.business.errors;

public class UserNotFoundException  extends RuntimeException  {
    public UserNotFoundException(String message) {
        super(message);
    }
}
