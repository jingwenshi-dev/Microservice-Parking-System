package ca.mcmaster.cas735.acmepark.gate.adapter;

import ca.mcmaster.cas735.acmepark.gate.dto.TransponderDTO;
import ca.mcmaster.cas735.acmepark.gate.port.TransponderReader;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AMQPTransponderReader {
    private final TransponderReader transponderReader;

    @Autowired
    public AMQPTransponderReader(TransponderReader transponderReader) {
        this.transponderReader = transponderReader;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "#{T(java.util.UUID).randomUUID().toString()}", autoDelete = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.transponder}", type = "topic"),
            key = "gate.*.transponder"
    ))

    public void listen(String raw) {
        transponderReader.readTransponder(translate(raw));
    }

    private TransponderDTO translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, TransponderDTO.class);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
