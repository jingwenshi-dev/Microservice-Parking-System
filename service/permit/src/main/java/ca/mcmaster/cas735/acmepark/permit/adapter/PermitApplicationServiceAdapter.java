package ca.mcmaster.cas735.acmepark.permit.adapter;

import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitRenewalDTO;
import ca.mcmaster.cas735.acmepark.permit.business.PermitApplicationService;
import ca.mcmaster.cas735.acmepark.permit.port.PermitApplicationPort;
import ca.mcmaster.cas735.acmepark.permit.port.PermitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermitApplicationServiceAdapter implements PermitApplicationPort {
    private final PermitApplicationService permitApplicationService;


    @Autowired
    public PermitApplicationServiceAdapter(PermitApplicationService permitApplicationService) {
        this.permitApplicationService = permitApplicationService;
    }

    @Override
    public void applyForPermit(PermitCreatedDTO permitDTO) throws Exception {
        // Call domain services to apply for a permit
        permitApplicationService.applyForPermit(permitDTO);
    }

    @Override
    public void renewPermit(PermitRenewalDTO renewalDTO) throws Exception {
        // Call domain services to renew a permit
        permitApplicationService.renewPermit(renewalDTO);
    }

    @Override
    public int getValidPermitCount(){
        return permitApplicationService.countValidPermits();
    }
}
