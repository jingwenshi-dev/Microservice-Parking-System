package ca.mcmaster.cas735.acmepark.gate.port;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;

public interface ValidationResultReceiver {
    void receive(GateCtrlDTO gateCtrl);
}
