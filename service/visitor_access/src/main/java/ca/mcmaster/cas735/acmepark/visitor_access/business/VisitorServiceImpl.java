package ca.mcmaster.cas735.acmepark.visitor_access.business;

import ca.mcmaster.cas735.acmepark.visitor_access.dto.VisitorRequest;
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
    public void handleVisitorEntry(String licensePlate) {
        try {
            VisitorRequest data = new VisitorRequest(licensePlate, true);
            visitorSender.sendOpenGateEntryRequest(data); // 通过 VisitorSender 发送进入请求
        } catch (Exception e) {
            log.error("handle visitor entry error:", e);
        }
    }

    // 处理访客退出逻辑
    @Override
    public void handleVisitorExit(String licensePlate) {
        try {
            VisitorRequest data = new VisitorRequest(licensePlate, false);
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
    }

    // 处理 Gate 服务的离开响应
    @Override
    public void handleGateExitResponse(String data) {
        // 处理离开请求的 Gate 响应逻辑
        log.info("Handling gate exit response: {}", data);
    }


    // 将原始 JSON 数据转换为 QR 码字符串
    @Override
    public String translate(String licensePlate, boolean entry) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (entry && StringUtils.hasLength(licensePlate)) {
                String qrCode = qrCodeService.generateQRCode(licensePlate);
                return mapper.readValue(qrCode, String.class);
            }
        } catch (Exception e) {
            log.error("some error in translate:", e);
        }
        return "";
    }
}