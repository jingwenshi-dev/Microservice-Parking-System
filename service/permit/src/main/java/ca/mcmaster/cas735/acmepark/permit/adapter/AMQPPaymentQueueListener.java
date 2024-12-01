package ca.mcmaster.cas735.acmepark.permit.adapter;
import ca.mcmaster.cas735.acmepark.permit.DTO.PermitCreatedDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class AMQPPaymentQueueListener{
    private final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> paymentStatusMap = new ConcurrentHashMap<>();
    @RabbitListener(queues = "payment.success.queue")

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
