package ca.mcmaster.cas735.acmepark.permit.adapter;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class AMQPPaymentServiceListener {
    private final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> paymentStatusMap = new ConcurrentHashMap<>();
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "payment.success.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.payment-response-permit-exchange}", ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))




    public void handlePaymentSuccess(PermitCreatedDTO event) {
        try {
            // Retrieve the CompletableFuture for the permit ID and complete it
            CompletableFuture<Boolean> future = paymentStatusMap.get(event.getPermitId());
            if (future != null) {
                future.complete(true);
            }
        } catch (Exception e) {
            System.err.println("Failed to process payment success event: " + e.getMessage());
        }
    }

    public boolean waitForPaymentSuccess(int permitId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        paymentStatusMap.put(permitId, future);
        try {
            // Wait for a maximum of 30 seconds
            return future.get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        } finally {
            paymentStatusMap.remove(permitId);
        }
    }


}
