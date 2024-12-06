package ca.mcmaster.cas735.acmepark.visitor_access.ports;

import ca.mcmaster.cas735.acmepark.gate.dto.ValidationDTO;

public interface ExitRequestHandler {
    void handleExit(ValidationDTO validationDTO);
}
