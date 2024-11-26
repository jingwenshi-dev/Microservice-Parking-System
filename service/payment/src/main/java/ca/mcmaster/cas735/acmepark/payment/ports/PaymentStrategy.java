package ca.mcmaster.cas735.acmepark.payment.ports;


import java.math.BigDecimal;

public interface PaymentStrategy {
    boolean pay(BigDecimal amount);

}
