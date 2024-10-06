package dev.jingwenshi.gate.business;

import dev.jingwenshi.gate.port.GateController;
import dev.jingwenshi.gate.port.PermitValidationResultReceiver;
import org.springframework.stereotype.Service;

@Service
public class GateResultHandler implements PermitValidationResultReceiver {

    private final GateController gateController;

    public GateResultHandler(GateController gateController) {
        this.gateController = gateController;
    }

    @Override
    public void receiveValidationResult(boolean open) {
        System.out.println("GateResultHandler: " + open);
        gateController.gateControl(open);
    }

}
