package ca.mcmaster.cas735.acmepark.gate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GateCtrlDTO {
    private String gateId;
    private String isValid;    // Prohibited using boolean since it is not supported by RabbitMQ
}
