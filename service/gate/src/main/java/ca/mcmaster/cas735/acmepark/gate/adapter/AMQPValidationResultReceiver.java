package ca.mcmaster.cas735.acmepark.gate.adapter;

import ca.mcmaster.cas735.acmepark.gate.port.PermitValidationResultReceiver;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPValidationResultReceiver {

    private final PermitValidationResultReceiver permitValidationResultReceiver;

    @Autowired
    public AMQPValidationResultReceiver(PermitValidationResultReceiver permitValidationResultReceiver) {
        this.permitValidationResultReceiver = permitValidationResultReceiver;
    }

    @RabbitListener(queuesToDeclare = @Queue("permit.validation.result.queue"))
    public void receiveValidationResult(String isValid) {
        boolean open = isValid.equals("valid");

        permitValidationResultReceiver.receiveValidationResult(open);
    }
}
