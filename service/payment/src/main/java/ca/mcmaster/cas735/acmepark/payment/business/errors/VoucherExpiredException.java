package ca.mcmaster.cas735.acmepark.payment.business.errors;

public class VoucherExpiredException extends Exception {
    public VoucherExpiredException(String type, String id) {
        super(String.format("%s %s has expired.", type, id));
    }
}