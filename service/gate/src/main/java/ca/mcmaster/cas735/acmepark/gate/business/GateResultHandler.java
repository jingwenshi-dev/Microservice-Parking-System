package ca.mcmaster.cas735.acmepark.gate.business;

import ca.mcmaster.cas735.acmepark.gate.port.GateController;
import ca.mcmaster.cas735.acmepark.gate.port.PermitValidationResultReceiver;
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
