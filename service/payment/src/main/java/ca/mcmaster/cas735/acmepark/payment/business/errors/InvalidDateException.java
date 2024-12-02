package ca.mcmaster.cas735.acmepark.payment.business.errors;

public class InvalidDateException extends Exception {
    public InvalidDateException(String message) {
        super(message);
    }
}
