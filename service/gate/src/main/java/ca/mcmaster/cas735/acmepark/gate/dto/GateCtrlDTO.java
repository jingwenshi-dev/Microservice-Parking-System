package ca.mcmaster.cas735.acmepark.gate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GateCtrlDTO {
    private String gateId;
    private Boolean isValid;
    private Boolean isEntry;
    private String qrCode;
}
