package ca.mcmaster.cas735.acmepark.permit.port;

public interface PaymentListenerPort {
    void handlePaymentSuccess(String data);
}
