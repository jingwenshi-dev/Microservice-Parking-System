package ca.mcmaster.cas735.acmepark.visitor_access.ports.provided;


import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;


public interface VisitorSender {

    void sendEntryResponseToGate(GateCtrlDTO gateCtrlDTO);

    void sendExitRequestToPayment(PaymentRequest paymentRequest);

    void sendExitRequestToGate(PaymentRequest paymentRequest);

}