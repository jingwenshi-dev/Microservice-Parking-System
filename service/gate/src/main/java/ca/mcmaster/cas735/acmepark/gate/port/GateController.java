package ca.mcmaster.cas735.acmepark.gate.port;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;

public interface GateController {
    void gateControl(GateCtrlDTO gateCtrl);
}
