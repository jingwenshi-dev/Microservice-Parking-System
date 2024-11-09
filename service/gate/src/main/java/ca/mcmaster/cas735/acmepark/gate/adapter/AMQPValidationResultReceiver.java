package ca.mcmaster.cas735.acmepark.gate.adapter;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.gate.port.PermitValidationResultReceiver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AMQPValidationResultReceiver {

    private final PermitValidationResultReceiver permitValidationResultReceiver;

    @Autowired
    public AMQPValidationResultReceiver(PermitValidationResultReceiver permitValidationResultReceiver) {
        this.permitValidationResultReceiver = permitValidationResultReceiver;
    }

    @RabbitListener(queuesToDeclare = @Queue("permit.validation.result.queue"))
    public void receiveValidationResult(String raw) {
        GateCtrlDTO gateCtrl = translate(raw);
        permitValidationResultReceiver.receiveValidationResult(gateCtrl);
    }

    private GateCtrlDTO translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, GateCtrlDTO.class);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
