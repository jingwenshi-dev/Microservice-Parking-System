package ca.mcmaster.cas735.acmepark.payment.ports.provided;


import java.math.BigDecimal;

public interface PaymentStrategy {
    boolean pay(BigDecimal amount);

}
