package ca.mcmaster.cas735.acmepark.gate.business;

import ca.mcmaster.cas735.acmepark.gate.dto.TransponderDTO;
import ca.mcmaster.cas735.acmepark.gate.port.ValidationReqSender;
import ca.mcmaster.cas735.acmepark.gate.port.TransponderReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateReqHandler implements TransponderReader {

    private final ValidationReqSender validationReqSender;

    @Autowired
    public GateReqHandler(ValidationReqSender validationReqSender) {
        this.validationReqSender = validationReqSender;
    }

    @Override
    public void readTransponder(TransponderDTO transponder) {
        validationReqSender.send(transponder);
    }
}