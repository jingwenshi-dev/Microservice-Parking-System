package ca.mcmaster.cas735.acmepark.gate.business;

import ca.mcmaster.cas735.acmepark.gate.dto.TransponderDTO;
import ca.mcmaster.cas735.acmepark.gate.port.PermitValidationReqSender;
import ca.mcmaster.cas735.acmepark.gate.port.TransponderReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateReqHandler implements TransponderReader {

    private final PermitValidationReqSender permitValidationReqSender;

    @Autowired
    public GateReqHandler(PermitValidationReqSender permitValidationReqSender) {
        this.permitValidationReqSender = permitValidationReqSender;
    }

    @Override
    public void readTransponder(TransponderDTO transponder) {
        permitValidationReqSender.validatePermit(transponder);
    }
}