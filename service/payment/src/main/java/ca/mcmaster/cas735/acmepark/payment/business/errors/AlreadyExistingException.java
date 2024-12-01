package ca.mcmaster.cas735.acmepark.payment.business.errors;

public class AlreadyExistingException extends  Exception {

    public AlreadyExistingException(String type, String id) {
        super(String.format("%s %s already exists.", type, id));
    }

}
