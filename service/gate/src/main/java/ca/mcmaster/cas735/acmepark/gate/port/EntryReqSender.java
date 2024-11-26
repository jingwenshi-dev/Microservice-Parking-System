package ca.mcmaster.cas735.acmepark.gate.port;

import ca.mcmaster.cas735.acmepark.gate.dto.TransponderDTO;

public interface EntryReqSender {
    void validateEntryReq(TransponderDTO transponder);
}
