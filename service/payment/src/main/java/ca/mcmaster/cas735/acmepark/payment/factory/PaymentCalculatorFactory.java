package ca.mcmaster.cas735.acmepark.payment.factory;

import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentCalculatorPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PaymentCalculatorFactory {

    private final Map<String, PaymentCalculatorPort> paymentCalculators;

    @Autowired
    public PaymentCalculatorFactory(List<PaymentCalculatorPort> paymentCalculatorList) {
        this.paymentCalculators = paymentCalculatorList.stream().collect(
                Collectors.toMap(calculator -> calculator.getClass().getSimpleName(), calculator -> calculator));
    }

    public PaymentCalculatorPort getPaymentCalculator(String userType) {
        switch (userType.toLowerCase()) {
            case "visitor":
                return paymentCalculators.get("HourlyPaymentCalculator");
            case "student":
            case "staff":
                return paymentCalculators.get("MonthlyPaymentCalculator");
            default:
                throw new IllegalArgumentException("Unsupported user type: " + userType);
        }
    }
}
