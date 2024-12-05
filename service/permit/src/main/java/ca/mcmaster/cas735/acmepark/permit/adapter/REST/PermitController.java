package ca.mcmaster.cas735.acmepark.permit.adapter.REST;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitRenewalDTO;
import ca.mcmaster.cas735.acmepark.permit.business.PermitApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permits")
public class PermitController {
    private final PermitApplicationService permitApplicationService;

    @Autowired
    public PermitController(PermitApplicationService permitApplicationService) {
        this.permitApplicationService = permitApplicationService;
    }

    @PostMapping("/apply")
    public ResponseEntity<String> applyForPermit(@RequestBody PermitCreatedDTO permitDTO) {
        try {
            permitApplicationService.applyForPermit(permitDTO);
            return new ResponseEntity<>("Permit application initiated. Payment processing in progress.",
                    HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to initiate permit application: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/renew")
    public ResponseEntity<String> renewPermit(@RequestBody PermitRenewalDTO renewalDTO) {
        permitApplicationService.renewPermit(renewalDTO);
        return new ResponseEntity<>("Permit renewal application initiated. Payment processing in progress.",
                HttpStatus.ACCEPTED);
    }

    @GetMapping("/valid-permits")
    public ResponseEntity<String> getValidPermitCount() {
        try {
            int validPermitCount = permitApplicationService.getValidPermitCount();
            return new ResponseEntity<>("Valid Permit Count: " + validPermitCount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error in validPermitCount", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}