package ca.mcmaster.cas735.acmepark.permit.port;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;

public interface PermitValidationResultSender {
    void sendValidationResult(GateCtrlDTO response);
}
