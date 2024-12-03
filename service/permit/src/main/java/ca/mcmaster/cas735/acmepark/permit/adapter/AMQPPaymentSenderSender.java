package ca.mcmaster.cas735.acmepark.permit.adapter;


import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.port.PaymentSenderPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class AMQPPaymentSenderSender implements PaymentSenderPort {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPPaymentSenderSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.custom.messaging.payment-request-exchange}")
    private String paymentRequestExchange;


    @Override
    public void initiatePayment(PermitCreatedDTO permit) {
        rabbitTemplate.convertAndSend(
                paymentRequestExchange,
                "*",
                permit
        );
    }

}
