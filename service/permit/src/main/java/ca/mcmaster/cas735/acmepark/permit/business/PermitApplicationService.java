package ca.mcmaster.cas735.acmepark.permit.business;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitRenewalDTO;
import ca.mcmaster.cas735.acmepark.permit.adapter.AMQPPaymentServiceListener;
import ca.mcmaster.cas735.acmepark.permit.business.entity.Permit;
import ca.mcmaster.cas735.acmepark.permit.business.entity.User;
import ca.mcmaster.cas735.acmepark.permit.port.PaymentServicePort;
import ca.mcmaster.cas735.acmepark.permit.port.PermitRepository;
import ca.mcmaster.cas735.acmepark.permit.port.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class PermitApplicationService{
    private final PaymentServicePort paymentServicePort;
    private final PermitRepository permitRepository;
    private final AMQPPaymentServiceListener amqpPaymentQueueListener;
    private final UserRepository userRepository;

    @Autowired
    public PermitApplicationService(PaymentServicePort paymentServicePort,
                                    PermitRepository permitRepository,
                                    UserRepository userRepository,
                                    AMQPPaymentServiceListener amqpPaymentQueueListener) {
        this.paymentServicePort = paymentServicePort;
        this.permitRepository = permitRepository;
        this.amqpPaymentQueueListener = amqpPaymentQueueListener;
        this.userRepository = userRepository;
    }


    public boolean applyForPermit(PermitCreatedDTO permitDTO) {
        // Logic to create a permit and generate the permit ID and transponder number
        String transponderNumber = generateTransponderNumber();

        permitDTO.setTransponderNumber(transponderNumber);


        // Send to payment service
        paymentServicePort.initiatePayment(permitDTO);

        // Wait for payment success event
        boolean paymentSuccess = amqpPaymentQueueListener.waitForPaymentSuccess(permitDTO.getPermitId());
        if (paymentSuccess) {
            storePermitData(permitDTO);  // Save permit to the database
            return true;
        }
        return false;
    }

    public boolean renewPermit(PermitRenewalDTO renewalDTO) {
        //Retrieve the permit
        Permit permit = permitRepository.findById(renewalDTO.getPermitId())
                .orElseThrow(() -> new RuntimeException("Permit not found"));

        // Prepare for payment
        PermitCreatedDTO paymentDTO = new PermitCreatedDTO();
        paymentDTO.setPermitId(permit.getPermitId());// Use the existing permit ID
        paymentDTO.setTransponderNumber(permit.getTransponderNumber());
        paymentDTO.setValidFrom(renewalDTO.getValidFrom());
        paymentDTO.setValidUntil(renewalDTO.getValidUntil());
        paymentDTO.setUserId(permit.getUser().getUserId());
        paymentDTO.setLotId(permit.getLotId());

        //Send to payment service
        paymentServicePort.initiatePayment(paymentDTO);

        // Wait for payment success
        boolean paymentSuccess = amqpPaymentQueueListener.waitForPaymentSuccess(permit.getPermitId());
        if (paymentSuccess) {
            //Update the permit validity dates
            permit.setValidFrom(renewalDTO.getValidFrom());
            permit.setValidUntil(renewalDTO.getValidUntil());

            //Save the updated permit
            permitRepository.save(permit);
            return true;
        }
        return false;
    }




    //method to store permit data
    private void storePermitData(PermitCreatedDTO permitDTO) {
        User user = userRepository.findById(permitDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Permit permit = new Permit(
                permitDTO.getTransponderNumber(),
                permitDTO.getValidFrom(),
                permitDTO.getValidUntil(),
                user,
                permitDTO.getLotId());
        permitRepository.save(permit);
    }






    private String generateTransponderNumber() {
        // Logic to generate a transponder number
        return "TN" + System.currentTimeMillis(); // Example transponder number
    }

}
