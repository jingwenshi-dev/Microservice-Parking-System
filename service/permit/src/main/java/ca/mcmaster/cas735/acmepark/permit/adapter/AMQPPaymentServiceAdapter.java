package ca.mcmaster.cas735.acmepark.permit.adapter;


import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.port.PaymentServicePort;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class AMQPPaymentServiceAdapter implements PaymentServicePort {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPPaymentServiceAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public void initiatePayment(PermitCreatedDTO permit) {
        rabbitTemplate.convertAndSend(
                "payment-request-exchange",
                "payment.process",
                permit
        );
    }

}
