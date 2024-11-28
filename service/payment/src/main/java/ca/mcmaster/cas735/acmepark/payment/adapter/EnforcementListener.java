package ca.mcmaster.cas735.acmepark.payment.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EnforcementListener {

    private final ParkingViolationsRepository repository;

    @Autowired
    public EnforcementListener(ParkingViolationsRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "enforcement.violation.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.enforcement-exchange}", ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))
    public void listen(String data) {
        log.debug("Received violation report: {}", data);
        ParkingViolationRequest request = translate(data);

        // 根据请求中的信息创建 ParkingViolation 实体
        ParkingViolation violation = new ParkingViolation();
        violation.setLicensePlate(request.getLicensePlate());
        violation.setViolationTime(LocalDateTime.now());
        violation.setFineAmount(request.getFineAmount());
        violation.setIsPaid(false);
        violation.setOfficerId(request.getOfficerId());
        violation.setLotId(request.getLotId());

        // 保存到数据库中
        repository.save(violation);
    }

    private ParkingViolationRequest translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, ParkingViolationRequest.class);
        } catch (Exception e) {
            log.error("Failed to parse violation request: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}