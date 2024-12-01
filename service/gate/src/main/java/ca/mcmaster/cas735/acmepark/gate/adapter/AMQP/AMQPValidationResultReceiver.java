package ca.mcmaster.cas735.acmepark.gate.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.gate.port.ValidationResultReceiver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPValidationResultReceiver {

    private final ValidationResultReceiver validationResultReceiver;

    @Autowired
    public AMQPValidationResultReceiver(ValidationResultReceiver validationResultReceiver) {
        this.validationResultReceiver = validationResultReceiver;
    }

    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = "validation.result.queue", durable = "false"),
                    exchange = @Exchange(value = "${app.custom.messaging.response.exchange}", ignoreDeclarationExceptions = "true", type = "topic"),
                    key = "*")
    )
    public void receiveValidationResult(String raw) throws NotFoundException, IllegalArgumentException {
        GateCtrlDTO gateCtrl = translate(raw);
        validationResultReceiver.receive(gateCtrl);
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
