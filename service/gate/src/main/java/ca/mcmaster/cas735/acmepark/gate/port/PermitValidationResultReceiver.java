package ca.mcmaster.cas735.acmepark.gate.port;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;

public interface PermitValidationResultReceiver {
    void receiveValidationResult(GateCtrlDTO gateCtrl);
}
