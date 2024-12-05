package ca.mcmaster.cas735.acmepark.permit.adapter.REST;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitRenewalDTO;
import ca.mcmaster.cas735.acmepark.permit.business.PermitProcessor;
import ca.mcmaster.cas735.acmepark.permit.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.permit.port.PermitManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PermitController {
    private final PermitManager permitManager;

    @Autowired
    public PermitController(PermitProcessor permitManager) {
        this.permitManager = permitManager;
    }

    @PostMapping("/apply")
    public ResponseEntity<String> applyForPermit(@RequestBody PermitCreatedDTO permitDTO) throws NotFoundException{
        permitManager.applyForPermit(permitDTO);
        return new ResponseEntity<>("Permit application initiated. Payment processing in progress.",
                HttpStatus.ACCEPTED);
    }

    @PutMapping("/renew")
    public ResponseEntity<String> renewPermit(@RequestBody PermitRenewalDTO renewalDTO) throws NotFoundException {
        permitManager.renewPermit(renewalDTO);
        return new ResponseEntity<>("Permit renewal application initiated. Payment processing in progress.",
                HttpStatus.ACCEPTED);
    }

    @GetMapping("/valid-permits")
    public ResponseEntity<String> getValidPermitCount() {
        try {
            int validPermitCount = permitManager.getValidPermitCount();
            return new ResponseEntity<>("Valid Permit Count: " + validPermitCount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error in validPermitCount", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}