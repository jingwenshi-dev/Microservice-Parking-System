package ca.mcmaster.cas735.acmepark.gate.adapter;

import ca.mcmaster.cas735.acmepark.gate.port.TransponderReader;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Service
public class AMQPTransponderReader {
    private final TransponderReader transponderReader;

    @Autowired
    public AMQPTransponderReader(TransponderReader transponderReader) {
        this.transponderReader = transponderReader;
    }

    @RabbitListener(queuesToDeclare = @Queue("transponder.queue"))
    public void listen(String transponderId) {
        transponderReader.readTransponder(transponderId);
    }
}
