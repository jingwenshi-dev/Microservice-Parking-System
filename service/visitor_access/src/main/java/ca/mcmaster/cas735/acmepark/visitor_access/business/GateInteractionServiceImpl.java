package ca.mcmaster.cas735.acmepark.visitor_access.business;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.gate.dto.ValidationDTO;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.visitor_access.business.entities.Visitor;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.GateInteractionHandler;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.QRCodeService;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.VisitorSender;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.required.VisitorDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class GateInteractionServiceImpl implements GateInteractionHandler {

    private final VisitorSender visitorSender;
    private final QRCodeService qrCodeService;
    private final VisitorDataRepository visitorDataRepository;

    private static final String VISITOR = "visitor";
    private static final String CREDIT_CARD_STRATEGY = "creditCard";

    @Autowired
    public GateInteractionServiceImpl(VisitorSender visitorSender, QRCodeService qrCodeService, VisitorDataRepository visitorDataRepository) {
        this.visitorSender = visitorSender;
        this.qrCodeService = qrCodeService;
        this.visitorDataRepository = visitorDataRepository;
    }

    // 处理 Gate 服务的进入响应
    @Override
    public void handleGateRequest(String data) {
        // 处理进入请求的 Gate 响应逻辑
        log.info("Handling gate entry response: {}", data);
        ValidationDTO validationDTO = translate(data);
        if (validationDTO.isEntry()) {
            handleEntryRequest(validationDTO);
        } else {
            handleExitRequest(validationDTO);
        }
    }

    // 处理 Gate 服务的离开响应
    private void handleExitRequest(ValidationDTO validationDTO) {
        // 查数据库，然后获取进入的时间,组装成交易请求
        PaymentRequest paymentRequest = getVisitorFromRepository(validationDTO);
        visitorSender.sendExitRequestToPayment(paymentRequest);
    }

    private void handleEntryRequest(ValidationDTO validationDTO) {
        GateCtrlDTO gateCtrlDTO = new GateCtrlDTO();
        if (validationDTO != null && validationDTO.isVisitorAllowed()) {
            //写入数据库进入时间。
            setVisitorToRepository(validationDTO);
            gateCtrlDTO.setIsValid(true);
            gateCtrlDTO.setGateId(validationDTO.getGateId());
            gateCtrlDTO.setLotId(validationDTO.getLotId());
            gateCtrlDTO.setIsEntry(validationDTO.isEntry());
            // 添加QR数据
            addQRCode(validationDTO, gateCtrlDTO);
        } else {
            gateCtrlDTO.setIsValid(false);
        }
        visitorSender.sendEntryResponseToGate(gateCtrlDTO);
    }

    // 将原始 JSON 数据转换为 QR 码字符串
    private void addQRCode(ValidationDTO validationDTO, GateCtrlDTO gateCtrlDTO) {
        try {
            if (validationDTO != null &&
                    validationDTO.isEntry() && StringUtils.hasLength(validationDTO.getLicensePlate())) {
                String qrCode = qrCodeService.generateQRCode(validationDTO.getLicensePlate());
                gateCtrlDTO.setQrCode(qrCode);
            }
        } catch (Exception e) {
            log.error("some error in translate:", e);
        }
    }

    private ValidationDTO translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, ValidationDTO.class);
        } catch (Exception e) {
            log.error("translate data error:", e);
            throw new RuntimeException(e);
        }
    }

    private void setVisitorToRepository(ValidationDTO validationDTO) {
        try {
            Visitor newVisitor = new Visitor();
            newVisitor.setLicensePlate(validationDTO.getLicensePlate());
            newVisitor.setEntryTime(LocalDateTime.now()); // 设置进入时间
            visitorDataRepository.save(newVisitor); // 保存新的访客
            log.info("New visitor created with LicensePlate: {}, EntryTime: {}", newVisitor.getLicensePlate(), newVisitor.getEntryTime());
        } catch (Exception ex) {
            log.error("add new user error:", ex);
        }
    }

    private PaymentRequest getVisitorFromRepository(ValidationDTO ValidationDTO) {
        PaymentRequest paymentRequest = new PaymentRequest();

        // 根据 licensePlate 查找 Visitor
        Optional<Visitor> visitorOpt = visitorDataRepository
                .findFirstByLicensePlateOrderByEntryTimeDesc(ValidationDTO.getLicensePlate());

        if (visitorOpt.isPresent()) {
            Visitor visitor = visitorOpt.get();
            paymentRequest.setEntryTime(visitor.getEntryTime());
            paymentRequest.setLicensePlate(visitor.getLicensePlate());
            paymentRequest.setUserType(VISITOR);
            paymentRequest.setLotId(ValidationDTO.getLotId());
            paymentRequest.setGateId(ValidationDTO.getGateId());
            paymentRequest.setPaymentMethod(CREDIT_CARD_STRATEGY);
            paymentRequest.setHourlyRate(ValidationDTO.getHourlyRate());
        }
        return paymentRequest;
    }
}
