package ca.mcmaster.cas735.acmepark.gate.adapter.REST;

import ca.mcmaster.cas735.acmepark.gate.port.provided.Monitor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void lookup(@RequestParam Long logId) {
        monitor.getParkingLotStatus(logId);
    }
}
