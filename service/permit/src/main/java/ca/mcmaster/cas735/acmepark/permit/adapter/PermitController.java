package ca.mcmaster.cas735.acmepark.permit.adapter;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitRenewalDTO;
import ca.mcmaster.cas735.acmepark.permit.business.PermitApplicationService;
import ca.mcmaster.cas735.acmepark.permit.port.PermitApplicationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permits")
public class PermitController {
    private final PermitApplicationPort permitApplicationPort;

    @Autowired
    public PermitController(PermitApplicationPort permitApplicationPort) {
        this.permitApplicationPort = permitApplicationPort;
    }

    @PostMapping("/apply")
    public ResponseEntity<String> applyForPermit(@RequestBody PermitCreatedDTO permitDTO) {
        System.out.println("Received payload: " + permitDTO);
        try {
        //Apply for the permit
            permitApplicationPort.applyForPermit(permitDTO);
            return new ResponseEntity<>("Permit application initiated. Payment processing in progress.", HttpStatus.ACCEPTED);

        //Return the result to the user
        } catch (Exception e) {
            // Handle error in permit application initiation
            return new ResponseEntity<>("Failed to initiate permit application: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/renew")
    public ResponseEntity<String> renewPermit(@RequestBody PermitRenewalDTO renewalDTO) {

        System.out.println("Received payload: " + renewalDTO);
        try {
            //Renewal for the permit
            permitApplicationPort.renewPermit(renewalDTO);
            return new ResponseEntity<>("Permit renewal application initiated. Payment processing in progress.", HttpStatus.ACCEPTED);

            //Return the result to the user
        } catch (Exception e) {
            // Handle error in permit application initiation
            return new ResponseEntity<>("Failed to initiate permit renewal application: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
