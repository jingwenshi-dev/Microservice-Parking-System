package ca.mcmaster.cas735.acmepark.gate.business.errors;


public class NotFoundException extends Exception {

    public NotFoundException(String lotId) {
        super(String.format("Parking lot %s not found.", lotId));
    }
}