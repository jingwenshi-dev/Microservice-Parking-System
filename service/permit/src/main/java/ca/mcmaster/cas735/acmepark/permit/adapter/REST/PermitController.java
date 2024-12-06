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

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PermitController {
    private final PermitManager permitManager;

    @Autowired
    public PermitController(PermitProcessor permitManager) {
        this.permitManager = permitManager;
    }

    @PostMapping("/apply")
    public ResponseEntity<Map<String, String>> applyForPermit(@RequestBody PermitCreatedDTO permitDTO) throws NotFoundException {
        permitManager.applyForPermit(permitDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "message", "Permit application successful!",
                        "licensePlate", permitDTO.getLicensePlate()));
    }

    @PutMapping("/renew")
    public ResponseEntity<Map<String, String>> renewPermit(@RequestBody PermitRenewalDTO renewalDTO) throws NotFoundException {
        permitManager.renewPermit(renewalDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "message", "Permit renewal successful!.",
                        "Permit Id",Integer.toString(renewalDTO.getPermitId())));
//        return new ResponseEntity<>("Permit renewal application initiated. Payment processing in progress.",
//                HttpStatus.ACCEPTED);
    }

    @GetMapping("/valid-permits")
    public ResponseEntity<Map<String, String>> getValidPermitCount() {
        try {
            int validPermitCount = permitManager.getValidPermitCount();
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(Map.of("Valid Permit Count:", Integer.toString(validPermitCount)));
//           return new ResponseEntity<>("Valid Permit Count: " + validPermitCount, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error in retrieving valid permit count"));
        }
    }
}