package ca.mcmaster.cas735.acmepark.payment.factory;

import ca.mcmaster.cas735.acmepark.payment.ports.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentStrategyFactory {

    private final Map<String, PaymentStrategy> paymentStrategies;

    @Autowired
    public PaymentStrategyFactory(Map<String, PaymentStrategy> paymentStrategies) {
        this.paymentStrategies = paymentStrategies;
    }

    public PaymentStrategy getPaymentStrategy(String paymentMethod) {
        PaymentStrategy strategy = paymentStrategies.get(paymentMethod.toLowerCase() + "PaymentStrategy");
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }
        return strategy;
    }
}
