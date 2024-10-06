package dev.jingwenshi.gate.business;

import dev.jingwenshi.gate.port.PermitValidationReqSender;
import dev.jingwenshi.gate.port.TransponderReader;
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
    public void readTransponder(String transponderId) {
        permitValidationReqSender.validatePermit(transponderId);
    }
}