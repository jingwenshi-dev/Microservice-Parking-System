package ca.mcmaster.cas735.acmepark.visitor_access.business;


import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.PaymentInteractionHandler;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.VisitorSender;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class PaymentInteractionImpl implements PaymentInteractionHandler {

    private final VisitorSender visitorSender;

    @Autowired
    public PaymentInteractionImpl(VisitorSender visitorSender) {
        this.visitorSender = visitorSender;
    }


    // Processing of payment results
    @Override
    public void handlePaymentResult(String data) {
        try {
            PaymentRequest paymentRequest= translate(data);
            GateCtrlDTO gateCtrlDTO = new GateCtrlDTO();
            paymentResultToGateCtrlDTO(paymentRequest, gateCtrlDTO);
            visitorSender.sendExitResponseToGate(gateCtrlDTO);
        } catch (Exception e) {
            log.error("handle visitor exit error:", e);
        }
    }

    private PaymentRequest translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Register the JavaTimeModule to handle LocalDateTime
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(raw, PaymentRequest.class);
        } catch (Exception e) {
            log.error("translate data error:", e);
            throw new RuntimeException(e);
        }
    }

    private void paymentResultToGateCtrlDTO(PaymentRequest paymentRequest, GateCtrlDTO gateCtrlDTO) {
        gateCtrlDTO.setGateId(paymentRequest.getGateId());
        gateCtrlDTO.setLotId(paymentRequest.getLotId());
        gateCtrlDTO.setIsValid(paymentRequest.isResult());
        gateCtrlDTO.setIsEntry(false);
    }

}