package dev.jingwenshi.permit.business;

import dev.jingwenshi.permit.port.PermitDBAccessor;
import dev.jingwenshi.permit.port.PermitValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermitService implements PermitValidator {
    private final PermitDBAccessor permitDBAccessor;

    @Autowired
    public PermitService(PermitDBAccessor permitDBAccessor) {
        this.permitDBAccessor = permitDBAccessor;
    }

    @Override
    public boolean validatePermit(String transponderId) {
        return permitDBAccessor.validPermit(transponderId);
    }

}
