package ca.mcmaster.cas735.acmepark.violation.business.errors;

public class NotFoundException extends Exception {

    public NotFoundException(long ticketNum, String licensePlate) {
        super(String.format("Ticket not found with number: %d and license plate: %s", ticketNum, licensePlate));
    }

}
