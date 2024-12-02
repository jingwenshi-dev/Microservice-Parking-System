package ca.mcmaster.cas735.acmepark.visitor_access.business;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.gate.dto.TransponderDTO;
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
        TransponderDTO transponderDTO = translate(data);
        if (transponderDTO.isEntry()) {
            handleEntryRequest(transponderDTO);
        } else {
            handleExitRequest(transponderDTO);
        }

    }

    // 处理 Gate 服务的离开响应
    public void handleExitRequest(TransponderDTO transponderDTO) {
        // 查数据库，然后获取进入的时间,组装成交易请求
        PaymentRequest paymentRequest = getVisitorFromRepository(transponderDTO);
        visitorSender.sendExitRequestToPayment(paymentRequest);
    }

    private void handleEntryRequest(TransponderDTO transponderDTO) {
        GateCtrlDTO gateCtrlDTO = new GateCtrlDTO();
        if (transponderDTO != null && transponderDTO.isVisitorAllowed()) {
            //写入数据库进入时间。
            setVisitorToRepository(transponderDTO);
            gateCtrlDTO.setIsValid(true);
            gateCtrlDTO.setGateId(transponderDTO.getGateId());
            // 添加QR数据
            addQRCode(transponderDTO, gateCtrlDTO);
        } else {
            gateCtrlDTO.setIsValid(false);
        }
        visitorSender.sendEntryResponseToGate(gateCtrlDTO);
    }

    // 将原始 JSON 数据转换为 QR 码字符串
    private void addQRCode(TransponderDTO transponderDTO, GateCtrlDTO gateCtrlDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (transponderDTO != null && transponderDTO.isEntry() && StringUtils.hasLength(transponderDTO.getLicensePlate())) {
                String qrCode = qrCodeService.generateQRCode(transponderDTO.getLicensePlate());
                gateCtrlDTO.setQrCode(qrCode);
            }
        } catch (Exception e) {
            log.error("some error in translate:", e);
        }

    }

    private TransponderDTO translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, TransponderDTO.class);
        } catch (Exception e) {
            log.error("translate data error:", e);
            throw new RuntimeException(e);
        }
    }

    private void setVisitorToRepository(TransponderDTO transponderDTO) {
        try {
            Visitor newVisitor = new Visitor();
            newVisitor.setLicensePlate(transponderDTO.getLicensePlate());
            newVisitor.setEntryTime(LocalDateTime.now()); // 设置进入时间
            visitorDataRepository.save(newVisitor); // 保存新的访客
            log.info("New visitor created with LicensePlate: {}, EntryTime: {}", newVisitor.getLicensePlate(), newVisitor.getEntryTime());
        } catch (Exception ex) {
            log.error("add new user error:", ex);
        }
    }

    private PaymentRequest getVisitorFromRepository(TransponderDTO transponderDTO) {
        PaymentRequest paymentRequest = new PaymentRequest();

        // 根据 licensePlate 查找 Visitor
        Optional<Visitor> visitorOpt = visitorDataRepository.findByLicensePlate(transponderDTO.getLicensePlate());

        if (visitorOpt.isPresent()) {
            Visitor visitor = visitorOpt.get();
            paymentRequest.builder()
                    .entryTime(visitor.getEntryTime())
                    .licensePlate(visitor.getLicensePlate())
                    .userType(VISITOR)
                    .gateId(transponderDTO.getGateId());
        }
        return paymentRequest;
    }
}
