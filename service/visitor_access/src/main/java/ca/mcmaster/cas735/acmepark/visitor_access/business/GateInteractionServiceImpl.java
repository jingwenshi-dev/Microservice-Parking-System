package ca.mcmaster.cas735.acmepark.visitor_access.business;

import ca.mcmaster.cas735.acmepark.gate.dto.ValidationDTO;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.GateInteractionHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class GateInteractionServiceImpl implements GateInteractionHandler {

    private final EntryRequestHandler entryRequestHandler;
    private final ExitRequestHandler exitRequestHandler;

    @Autowired
    public GateInteractionServiceImpl(EntryRequestHandler entryRequestHandler,
                                      ExitRequestHandler exitRequestHandler) {
        this.exitRequestHandler = exitRequestHandler;
        this.entryRequestHandler = entryRequestHandler;

    }

    // Processing Gate Service Entry Responses
    @Override
    public void handleGateRequest(String data) {
        log.info("Handling gate entry response: {}", data);
        ValidationDTO validationDTO = translate(data);
        if (validationDTO.isEntry()) {
            entryRequestHandler.handleEntry(validationDTO);
        } else {
            exitRequestHandler.handleExit(validationDTO);
        }
    }

    private ValidationDTO translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(raw, ValidationDTO.class);
        } catch (Exception e) {
            log.error("translate data error:", e);
            throw new RuntimeException(e);
        }
    }

}
