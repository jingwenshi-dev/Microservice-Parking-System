package ca.mcmaster.cas735.acmepark.gate.business;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
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
    public void receiveValidationResult(GateCtrlDTO gateCtrl) {
        System.out.println("GateResultHandler: " + gateCtrl.getIsValid());
        gateController.gateControl(gateCtrl);
    }

}
