package ca.mcmaster.cas735.acmepark.gate.business;

import ca.mcmaster.cas735.acmepark.gate.dto.TransponderDTO;
import ca.mcmaster.cas735.acmepark.gate.port.EntryReqSender;
import ca.mcmaster.cas735.acmepark.gate.port.TransponderReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateReqHandler implements TransponderReader {

    private final EntryReqSender entryReqSender;

    @Autowired
    public GateReqHandler(EntryReqSender entryReqSender) {
        this.entryReqSender = entryReqSender;
    }

    @Override
    public void readTransponder(TransponderDTO transponder) {
        entryReqSender.validateEntryReq(transponder);
    }
}