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


    // 处理支付结果
    @Override
    public void handlePaymentResult(String data) {
        try {
            PaymentRequest paymentRequest= translate(data);
            GateCtrlDTO gateCtrlDTO = new GateCtrlDTO();
            gateCtrlDTO.setIsValid(paymentRequest.isResult());
            visitorSender.sendExitRequestToGate(paymentRequest); // 通过 VisitorSender 发送离开请求
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

}