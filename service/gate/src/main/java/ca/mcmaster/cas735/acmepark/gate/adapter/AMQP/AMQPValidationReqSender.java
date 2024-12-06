package ca.mcmaster.cas735.acmepark.gate.adapter.AMQP;

import ca.mcmaster.cas735.acmepark.gate.dto.ParkingLotDTO;
import ca.mcmaster.cas735.acmepark.gate.dto.TransponderDTO;
import ca.mcmaster.cas735.acmepark.gate.dto.ValidationDTO;
import ca.mcmaster.cas735.acmepark.gate.port.ValidationReqSender;
import ca.mcmaster.cas735.acmepark.gate.port.provided.Monitor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPValidationReqSender implements ValidationReqSender {

    private final RabbitTemplate rabbitTemplate;
    private final Monitor monitor;

    @Autowired
    public AMQPValidationReqSender(RabbitTemplate rabbitTemplate, Monitor monitor) {
        this.rabbitTemplate = rabbitTemplate;
        this.monitor = monitor;
    }

    @Value("${app.custom.messaging.visitor.exchange.request}")
    private String visitorExchange;

    @Value("${app.custom.messaging.permit.exchange.request}")
    private String permitExchange;

    @Override
    public void send(TransponderDTO transponder) {

        try {
            // Get then warp parking lot info such as hourly rate, visitor allowed with transponder info into validationDTO.
            ParkingLotDTO parkingLot = monitor.getParkingLotInfo(transponder.getLotId());
            ValidationDTO validation = new ValidationDTO(transponder, parkingLot);

            if (transponder.getTransponderId() == null || transponder.getTransponderId().isEmpty()) {
                rabbitTemplate.convertAndSend(
                        visitorExchange,
                        "*",
                        translate(validation)
                );
            }
            else {
                rabbitTemplate.convertAndSend(
                        permitExchange,
                        "*",
                        translate(validation)
                );
            }

        } catch (Exception e) {
            // If parking lot not found, ignore the invalid request, which should never happen.
            log.error("Build ValidationDTO Failed:",e);
        }
    }

    private String translate(ValidationDTO validation) {
        ObjectMapper mapper= new ObjectMapper();
        try {
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.writeValueAsString(validation);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}