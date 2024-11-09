package ca.mcmaster.cas735.acmepark.gate.port;


import ca.mcmaster.cas735.acmepark.gate.dto.TransponderDTO;

public interface TransponderReader {
    void readTransponder(TransponderDTO transponder);
}
