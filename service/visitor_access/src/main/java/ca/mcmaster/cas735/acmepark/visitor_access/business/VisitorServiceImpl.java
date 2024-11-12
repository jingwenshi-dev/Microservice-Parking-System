package ca.mcmaster.cas735.acmepark.visitor_access.business;

import ca.mcmaster.cas735.acmepark.visitor_access.dto.GateAccessRequest;

import ca.mcmaster.cas735.acmepark.visitor_access.ports.VisitorSender;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.QRCodeService;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.VisitorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class VisitorServiceImpl implements VisitorService {

    private final VisitorSender visitorSender;

    private final QRCodeService qrCodeService;

    @Autowired
    public VisitorServiceImpl(VisitorSender visitorSender, QRCodeService qrCodeService) {
        this.visitorSender = visitorSender;
        this.qrCodeService = qrCodeService;
    }

    // 处理访客进入逻辑
    @Override
    public void handleVisitorEntry(String data) {
        try {

            visitorSender.sendOpenGateEntryRequest(data); // 通过 VisitorSender 发送进入请求
        } catch (Exception e) {
            log.error("handle visitor entry error:", e);
        }
    }

    // 处理访客退出逻辑
    @Override
    public void handleVisitorExit(String data) {
        try {
            visitorSender.sendOpenGateExitRequest(data); // 通过 VisitorSender 发送离开请求
        } catch (Exception e) {
            log.error("handle visitor exit error:", e);
        }
    }

    // 处理 Gate 服务的进入响应
    @Override
    public void handleGateEntryResponse(String data) {
        // 处理进入请求的 Gate 响应逻辑
        log.info("Handling gate entry response: {}", data);
        GateAccessRequest gateAccessRequest = translate(data);
        addQRCode(gateAccessRequest);
        visitorSender.sendGateEntryResponseToVisitor(gateAccessRequest);
    }

    // 处理 Gate 服务的离开响应
    @Override
    public void handleGateExitResponse(String data) {
        // 处理离开请求的 Gate 响应逻辑
        log.info("Handling gate exit response: {}", data);
        GateAccessRequest gateAccessRequest = translate(data);
        visitorSender.sendGateExitResponseToVisitor(gateAccessRequest);
    }


    // 将原始 JSON 数据转换为 QR 码字符串
    private void addQRCode(GateAccessRequest gateAccessRequest) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (gateAccessRequest != null
                    && gateAccessRequest.isValid()
                    && StringUtils.hasLength(gateAccessRequest.getLicensePlate())) {
                String qrCode = qrCodeService.generateQRCode(gateAccessRequest.getLicensePlate());
                gateAccessRequest.setQrCode(qrCode);
            }
        } catch (Exception e) {
            log.error("some error in translate:", e);
        }
    }

    private GateAccessRequest translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, GateAccessRequest.class);
        } catch (Exception e) {
            log.error("translate data error:", e);
            throw new RuntimeException(e);
        }
    }

}