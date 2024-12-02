package ca.mcmaster.cas735.acmepark.permit.adapter;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitRenewalDTO;
import ca.mcmaster.cas735.acmepark.permit.business.PermitApplicationService;
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
    private final PermitApplicationService permitService;

    @Autowired
    public PermitController(PermitApplicationService permitService) {
        this.permitService = permitService;
    }

    @PostMapping("/apply")
    public ResponseEntity<String> applyForPermit(@RequestBody PermitCreatedDTO permitDTO) {
        System.out.println("Received payload: " + permitDTO);
        //Apply for the permit
        boolean isPermitCreated = permitService.applyForPermit(permitDTO);

        //Return the result to the user
        if (isPermitCreated) {
            return new ResponseEntity<>("Permit and payment processed successfully.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Permit created, but payment failed.", HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping("/renew")
    public ResponseEntity<String> renewPermit(@RequestBody PermitRenewalDTO renewalDTO) {
        boolean renewalSuccess = permitService.renewPermit(renewalDTO);

        if (renewalSuccess) {
            return new ResponseEntity<>("Permit renewed successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Permit renewal failed due to payment issues.", HttpStatus.BAD_REQUEST);
        }
    }




}
