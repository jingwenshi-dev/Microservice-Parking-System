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
    private final ConcurrentHashMap<String, Boolean> paymentStatusMap = new ConcurrentHashMap<>();
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "payment.success.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.payment-response-permit-exchange}", ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))
    public void handlePaymentSuccess(PermitCreatedDTO event) {
        try {
            // Update the payment status map with the result from the event
            paymentStatusMap.put(event.getLicensePlate(), event.isResult());
        } catch (Exception e) {
            System.err.println("Failed to process payment success event: " + e.getMessage());
        }
    }


    public boolean waitForPaymentSuccess(String licensePlate) {
        try {
            // Poll the status map for the license plate for up to 30 seconds
            long startTime = System.currentTimeMillis();
            long timeout = 5 * 60 * 1000; // 5 minutes in milliseconds
            while (System.currentTimeMillis() - startTime < timeout) {
                Boolean result = paymentStatusMap.remove(licensePlate);
                if (result != null) {
                    return result; // Return the result as soon as it's available
                }
                Thread.sleep(100); // Small delay to avoid tight looping
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for payment success: " + e.getMessage());
        }
        return false; // Return false if no result within 30 seconds
    }


}
