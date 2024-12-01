package ca.mcmaster.cas735.acmepark.gate.business;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.gate.port.GateController;
import ca.mcmaster.cas735.acmepark.gate.port.ValidationResultReceiver;
import ca.mcmaster.cas735.acmepark.gate.port.provided.Monitor;
import org.springframework.stereotype.Service;

@Service
public class GateResultHandler implements ValidationResultReceiver {

    private final GateController gateController;
    private final Monitor monitor;

    public GateResultHandler(GateController gateController, Monitor monitor) {
        this.gateController = gateController;
        this.monitor = monitor;
    }

    @Override
    public void receive(GateCtrlDTO gateCtrl) {
        gateController.gateControl(gateCtrl);
        monitor.recordOccupancy(gateCtrl.getLotId(), gateCtrl.getIsEntry());
    }

}
