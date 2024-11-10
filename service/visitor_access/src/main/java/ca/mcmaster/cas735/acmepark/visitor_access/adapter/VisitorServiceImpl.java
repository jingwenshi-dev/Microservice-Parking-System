package ca.mcmaster.cas735.acmepark.visitor_access.adapter;

import ca.mcmaster.cas735.acmepark.visitor_access.domain.PaymentRequest;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.GateClient;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.VisitorService;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class VisitorServiceImpl implements VisitorService {

    private final GateClient gateClient;
    private final Map<String, String> qrCodeToSession = new HashMap<>(); // 存储 QR 码与会话状态的映射
    private final Map<String, Boolean> visitorPassPaymentStatus = new HashMap<>(); // 存储访客支付状态的映射

    public VisitorServiceImpl(GateClient gateClient) {
        this.gateClient = gateClient;
    }

    // 处理访客进入逻辑
    @Override
    public void handleVisitorEntry(String qrCode) {
        // 为访客创建 QR 码会话
        qrCodeToSession.put(qrCode, "SessionActive");
        gateClient.sendOpenGateRequest("open_gate:visitor"); // 通过 GateClient 发送开门请求
    }

    // 处理访客退出逻辑
    @Override
    public void handleVisitorExit(String qrCode) {
        if (!qrCodeToSession.containsKey(qrCode)) {
            log.debug("退出被拒绝: 系统中找不到 QR 码");
            return;
        }

        // 验证支付或访客通行证
        if (visitorPassPaymentStatus.containsKey(qrCode) && visitorPassPaymentStatus.get(qrCode)) {
            // 访客离开时移除 QR 码会话
            qrCodeToSession.remove(qrCode);
            gateClient.sendOpenGateRequest("open_gate:visitor"); // 通过 GateClient 发送开门请求
            log.debug("访客退出成功");
        } else {
            log.debug("退出被拒绝: 支付未完成");
        }
    }

    // 处理访客支付逻辑
    @Override
    public void processVisitorPayment(String qrCode, boolean paymentStatus) {
        visitorPassPaymentStatus.put(qrCode, paymentStatus); // 更新支付状态
    }

    // 将原始 JSON 数据转换为 QR 码字符串
    @Override
    public String translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 将原始 JSON 数据转换为 PaymentRequest 对象
    @Override
    public PaymentRequest translatePayment(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, PaymentRequest.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
