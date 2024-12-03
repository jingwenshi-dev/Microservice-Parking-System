package ca.mcmaster.cas735.acmepark.permit.adapter;


import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import ca.mcmaster.cas735.acmepark.permit.business.entity.User;
import ca.mcmaster.cas735.acmepark.permit.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.permit.port.PaymentSenderPort;
import ca.mcmaster.cas735.acmepark.permit.port.PermitRepository;
import ca.mcmaster.cas735.acmepark.permit.port.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

@Service
public class AMQPPaymentSender implements PaymentSenderPort {
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    public AMQPPaymentSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;


    }

    @Value("${app.custom.messaging.payment-request-exchange}")
    private String paymentRequestExchange;


    @Override
    public void initiatePayment(PermitCreatedDTO permitDTO) {

        rabbitTemplate.convertAndSend(
                paymentRequestExchange,
                "*",
                permitDTO
        );
    }


}
