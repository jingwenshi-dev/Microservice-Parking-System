package ca.mcmaster.cas735.acmepark.payment.adapter;

import ca.mcmaster.cas735.acmepark.payment.business.entities.ParkingViolation;
import ca.mcmaster.cas735.acmepark.payment.ports.required.ParkingViolationsRepository;
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

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "enforcement.violation.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.add-parking-violation-data}",
                    ignoreDeclarationExceptions = "true", type = "topic"), key = "*"))
    public void listen(String data) {
        log.debug("Received violation report: {}", data);
        ParkingViolation request = translate(data);

        // Create the ParkingViolation entity based on the information in the request
        ParkingViolation violation = new ParkingViolation();
        violation.setLicensePlate(request.getLicensePlate());
        violation.setViolationTime(LocalDateTime.now());
        violation.setFineAmount(request.getFineAmount());
        violation.setIsPaid(false);
        violation.setOfficerId(request.getOfficerId());
        violation.setLotId(request.getLotId());

        // Save to database
        repository.save(violation);
    }

    private ParkingViolation translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, ParkingViolation.class);
        } catch (Exception e) {
            log.error("Failed to parse violation request: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}