package ca.mcmaster.cas735.acmepark.payment.business.errors;

public class NotFoundException extends Exception {
    public NotFoundException(String type, String id) {
        super(String.format("%s %s not found.", type, id));
    }
}