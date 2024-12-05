package ca.mcmaster.cas735.acmepark.permit.business;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitRenewalDTO;
import ca.mcmaster.cas735.acmepark.permit.adapter.AMQP.AMQPPaymentSender;
import ca.mcmaster.cas735.acmepark.permit.business.entity.Permit;
import ca.mcmaster.cas735.acmepark.permit.business.entity.User;
import ca.mcmaster.cas735.acmepark.permit.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.permit.port.PermitApplicationPort;
import ca.mcmaster.cas735.acmepark.permit.port.PermitDataRepo;
import ca.mcmaster.cas735.acmepark.permit.port.UserDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class PermitApplicationService implements PermitApplicationPort {
    private final AMQPPaymentSender amqpPaymentSender;
    private final PermitDataRepo permitDB;
    private final UserDataRepo userDB;

    @Autowired
    public PermitApplicationService(AMQPPaymentSender amqpPaymentSender, PermitDataRepo permitDB, UserDataRepo userDB) {
        this.amqpPaymentSender = amqpPaymentSender;
        this.permitDB = permitDB;
        this.userDB = userDB;
    }

    @Override
    public void applyForPermit(PermitCreatedDTO permitDTO) {
        // Logic to generate transponder number
        UUID transponderNumber = UUID.randomUUID();
        permitDTO.setTransponderNumber(transponderNumber);
        permitDTO.setPermitType("APPLY");

        // Fetch the user entity based on the userId from the PermitCreatedDTO
        User user = userDB.findByUserId(permitDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User with ID " + permitDTO.getUserId() + " not found"));

        // Set the userType based on the User entity
        permitDTO.setUserType(user.getUserType().toString());

        // Send to payment service
        initiatePayment(permitDTO);

        System.out.println("Permit application submitted and payment initiated for User ID: "
                + permitDTO.getUserId());
    }

    @Override
    public void renewPermit(PermitRenewalDTO renewalDTO) {
        //Retrieve the permit
        Permit permit = permitDB.findById(renewalDTO.getPermitId()).orElseThrow(
                () -> new NotFoundException("Permit with ID " + renewalDTO.getPermitId() + " not found"));

        // Prepare for payment
        PermitCreatedDTO paymentDTO = new PermitCreatedDTO();
        paymentDTO.setPermitType("RENEW");
        paymentDTO.setTransponderNumber(permit.getTransponderNumber());
        paymentDTO.setValidFrom(renewalDTO.getValidFrom());
        paymentDTO.setValidUntil(renewalDTO.getValidUntil());
        paymentDTO.setUserId(permit.getUser().getUserId());
        paymentDTO.setLotId(permit.getLotId());
        paymentDTO.setUserType(permit.getUser().getUserType().toString());
        paymentDTO.setPaymentMethod(renewalDTO.getPaymentMethod());
        paymentDTO.setLicensePlate(permit.getLicensePlate());

        //Send to payment service
        initiatePayment(paymentDTO);
        System.out.println("Permit application submitted and payment initiated for User ID: " + paymentDTO.getUserId());
    }

    @Override
    public int getValidPermitCount() {
        List<Permit> allPermits = permitDB.findAll();
        LocalDateTime today = LocalDateTime.now();

        long validPermitCount = allPermits.stream().filter(permit -> permit.getValidFrom() != null
                && permit.getValidUntil() != null).filter(permit -> !permit.getValidFrom().isAfter(today) && !permit.getValidUntil().isBefore(today)).count();

        return (int) validPermitCount;
    }

    public void initiatePayment(PermitCreatedDTO permitDTO) {
        // Logic for initiating the payment (e.g., sending a message to RabbitMQ)
        amqpPaymentSender.initiatePayment(permitDTO);
        System.out.println("Payment initiation for Permit ID: " + permitDTO.getUserId());
    }

    @Override
    public void processPaymentSuccess(PermitCreatedDTO event) {
        System.out.println("Checking and storing for permit: " + event);
        boolean paymentSuccess = event.isResult();

        if (paymentSuccess) {
            try {
                // Proceed with storing the permit if payment is successful
                storePermitData(event);
                System.out.println("Permit Application Success for License Plate: " + event.getLicensePlate());
            } catch (Exception e) {
                System.err.println("Error while storing permit data: " + e.getMessage());
                // Handle storage failure, e.g., retry or raise an alert
            }
        } else {
            // Log or handle the payment failure
            System.err.println("Payment failed for Permit for: " + event.getLicensePlate());
            handlePaymentFailure(event);
        }
    }

    private void handlePaymentFailure(PermitCreatedDTO event) {
        System.out.println("Notifying about payment failure for permit: " + event.getLicensePlate());
        //can future add handle method
    }

    //method to store permit data
    private void storePermitData(PermitCreatedDTO permitDTO) {
        User user = userDB.findById(permitDTO.getUserId()).orElseThrow(() -> new NotFoundException("User with ID " + permitDTO.getUserId() + " not found"));
        if ("APPLY".equalsIgnoreCase(permitDTO.getPermitType())) {
            Permit permit = new Permit();
            permit.setTransponderNumber(permitDTO.getTransponderNumber());
            permit.setValidFrom(permitDTO.getValidFrom());
            permit.setValidUntil(permitDTO.getValidUntil());
            permit.setUser(user);
            permit.setLotId(permitDTO.getLotId());
            permit.setLicensePlate(permitDTO.getLicensePlate());
            permitDB.save(permit);
            System.out.println("Permit save for Permit id: " + permit.getPermitId());
        } else if ("RENEW".equalsIgnoreCase(permitDTO.getPermitType())) {
            // Handle RENEW permit type
            Permit existingPermit = permitDB.findByLicensePlate(permitDTO.getLicensePlate()).orElseThrow(() -> new NotFoundException("Permit with License Plate " + permitDTO.getLicensePlate() + " not found"));

            // Update validFrom and validUntil
            existingPermit.setValidFrom(permitDTO.getValidFrom());
            existingPermit.setValidUntil(permitDTO.getValidUntil());

            permitDB.save(existingPermit);
            System.out.println("Permit renewed for Permit ID: " + existingPermit.getPermitId());

        } else {
            throw new IllegalArgumentException("Invalid permit type: " + permitDTO.getPermitType());
        }
    }
}
