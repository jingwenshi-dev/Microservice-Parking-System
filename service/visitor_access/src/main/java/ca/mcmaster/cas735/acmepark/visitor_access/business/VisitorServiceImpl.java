package ca.mcmaster.cas735.acmepark.visitor_access.business;


import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.VisitorRequestHandler;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.VisitorSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class VisitorServiceImpl implements VisitorRequestHandler {

    private final VisitorSender visitorSender;

    @Autowired
    public VisitorServiceImpl(VisitorSender visitorSender) {
        this.visitorSender = visitorSender;
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

}