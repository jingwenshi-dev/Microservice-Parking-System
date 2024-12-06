package ca.mcmaster.cas735.acmepark.visitor_access.adapter;


import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.PaymentInteractionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Listening for messages related to user requests
 */
@Service
@Slf4j
public class AMQPPaymentMessageListener {

    // Inject VisitorService to handle business logic
    private final PaymentInteractionHandler paymentInteractionService;

    @Autowired
    public AMQPPaymentMessageListener(PaymentInteractionHandler paymentInteractionService) {
        this.paymentInteractionService = paymentInteractionService;
    }

    // Listen to the data returned by the transaction
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "PaymentToVisitorExitQueue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.payment-response-visitor-exchange}", ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))
    public void listenPaymentForVisitorExit(String data) {
        log.debug("Receive the results returned by the transaction.{}", data);
        paymentInteractionService.handlePaymentResult(data);
    }

}