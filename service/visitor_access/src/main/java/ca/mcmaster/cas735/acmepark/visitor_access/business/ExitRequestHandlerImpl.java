package ca.mcmaster.cas735.acmepark.visitor_access.business;

import ca.mcmaster.cas735.acmepark.gate.dto.ValidationDTO;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.visitor_access.business.entities.Visitor;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.VisitorSender;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.required.VisitorDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ExitRequestHandlerImpl implements ExitRequestHandler {

    private final VisitorSender visitorSender;
    private final VisitorDataRepository visitorDataRepository;

    private static final String VISITOR = "visitor";
    private static final String CREDIT_CARD_STRATEGY = "creditCard";

    @Autowired
    public ExitRequestHandlerImpl(VisitorSender visitorSender, VisitorDataRepository visitorDataRepository) {
        this.visitorSender = visitorSender;
        this.visitorDataRepository = visitorDataRepository;
    }

    @Override
    public void handleExit(ValidationDTO validationDTO) {
        // 查数据库，然后获取进入的时间,组装成交易请求
        PaymentRequest paymentRequest = getVisitorFromRepository(validationDTO);
        visitorSender.sendExitRequestToPayment(paymentRequest);
    }

    private PaymentRequest getVisitorFromRepository(ValidationDTO validationDTO) {
        PaymentRequest paymentRequest = new PaymentRequest();

        // 根据 licensePlate 查找 Visitor
        Optional<Visitor> visitorOpt = visitorDataRepository.
                findFirstByLicensePlateOrderByEntryTimeDesc(validationDTO.getLicensePlate());

        if (visitorOpt.isPresent()) {
            Visitor visitor = visitorOpt.get();
            paymentRequest.setEntryTime(visitor.getEntryTime());
            paymentRequest.setLicensePlate(visitor.getLicensePlate());
            paymentRequest.setUserType(VISITOR);
            paymentRequest.setLotId(validationDTO.getLotId());
            paymentRequest.setGateId(validationDTO.getGateId());
            paymentRequest.setPaymentMethod(CREDIT_CARD_STRATEGY);
            paymentRequest.setHourlyRate(validationDTO.getHourlyRate());
        }
        return paymentRequest;
    }
}
