package ca.mcmaster.cas735.acmepark.visitor_access.adapter;

import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.GateInteractionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Listening for Gate service-related responses
 */
@Service
@Slf4j
public class AMQPGateMessageListener {

    // Inject VisitorService to handle business logic
    private final GateInteractionHandler gateInteractionServiceImpl;

    @Autowired
    public AMQPGateMessageListener(GateInteractionHandler gateInteractionServiceImpl) {
        this.gateInteractionServiceImpl = gateInteractionServiceImpl;
    }

    // Listening to the Gate service
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "gateEntryResponseQueue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.gate-to-visitor-exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))
    public void listenForGateEntryRequest(String data) {
        log.debug("Receive an incoming response from the Gate service: {}", data);
        // Call the VisitorService to process the results returned by the Gate.
        gateInteractionServiceImpl.handleGateRequest(data);
    }

}
