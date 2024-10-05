package dev.jingwenshi.gate.business;

import dev.jingwenshi.gate.port.GateController;
import dev.jingwenshi.gate.port.PermitValidator;
import dev.jingwenshi.gate.port.TransponderReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateService implements TransponderReader {

    private final GateController gateController;
    private final PermitValidator permitValidator;

    @Autowired
    public GateService(GateController gateController, PermitValidator permitValidator) {
        this.gateController = gateController;
        this.permitValidator = permitValidator;
    }

    @Override
    public void readTransponder(String transponderId) {
        gateController.gateControl(permitValidator.validatePermit(transponderId));
    }
}