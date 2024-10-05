package dev.jingwenshi.permit.adapter;

import dev.jingwenshi.permit.port.PermitDBAccessor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CassandraAccessor implements PermitDBAccessor {

    @Override
    public boolean validPermit(String transponderId) {
        return Objects.equals(transponderId, "1");
    }
}
