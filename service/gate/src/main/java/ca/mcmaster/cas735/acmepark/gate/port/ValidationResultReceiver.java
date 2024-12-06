package ca.mcmaster.cas735.acmepark.gate.port;

import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;

public interface ValidationResultReceiver {
    void receive(GateCtrlDTO gateCtrl) throws NotFoundException;
}
