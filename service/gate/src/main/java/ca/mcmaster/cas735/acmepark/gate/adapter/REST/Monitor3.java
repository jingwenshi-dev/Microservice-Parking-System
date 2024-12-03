package ca.mcmaster.cas735.acmepark.gate.adapter.REST;

import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.gate.dto.LotOccupancyDTO;
import ca.mcmaster.cas735.acmepark.gate.port.provided.Monitor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@Tag(name = "Monitor parking lot status")
@RequestMapping(value = "/monitor")
public class Monitor3 {

    private final Monitor monitor;

    @Autowired
    public Monitor3(Monitor monitor) {
        this.monitor = monitor;
    }

    @GetMapping(value = "/lookup")
    @Operation(description = "Lookup for tickets with a given ticket id and license plate")
    public EntityModel<LotOccupancyDTO> lookup(@RequestParam Long lotId) throws NotFoundException {
        return asEntity(monitor.getParkingLotStatus(lotId));
    }

    private EntityModel<LotOccupancyDTO> asEntity(LotOccupancyDTO data) {
        return EntityModel.of(data);
    }
}
