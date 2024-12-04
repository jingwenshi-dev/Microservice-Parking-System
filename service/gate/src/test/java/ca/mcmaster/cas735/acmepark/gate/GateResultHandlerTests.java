package ca.mcmaster.cas735.acmepark.gate;

import ca.mcmaster.cas735.acmepark.gate.business.Dashboard;
import ca.mcmaster.cas735.acmepark.gate.business.GateReqHandler;
import ca.mcmaster.cas735.acmepark.gate.business.GateResultHandler;
import ca.mcmaster.cas735.acmepark.gate.business.LotOccupancyHandler;
import ca.mcmaster.cas735.acmepark.gate.business.entities.LotOccupancy;
import ca.mcmaster.cas735.acmepark.gate.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.gate.port.GateController;
import ca.mcmaster.cas735.acmepark.gate.port.provided.LotOccupancyPort;
import ca.mcmaster.cas735.acmepark.gate.port.required.LotOccupancyDataRepo;
import ca.mcmaster.cas735.acmepark.gate.port.required.ParkingLotDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GateResultHandlerTests {

    @Mock
    private GateController gateController;

    @Mock
    private LotOccupancyDataRepo lotOccupancyDB;

    @Mock
    private ParkingLotDataRepo parkingLotDB;

    @Mock
    private LotOccupancyPort lotOccupancyPort;

    private GateResultHandler gateResultHandler;

    @BeforeEach
    void setUp() {
        Dashboard dashboard = new Dashboard(parkingLotDB, lotOccupancyDB, lotOccupancyPort);
        gateResultHandler = new GateResultHandler(gateController, dashboard);
    }

    /**
     * Verifies that the `GateResultHandler` class correctly sends a valid gate control result exactly once.
     * Verifies that the occurrence of the current occupancy is recorded exactly once by `monitor`.
     */
    @Test
    void testReceiveValidResult() throws NotFoundException {
        // Arrange
        GateCtrlDTO gateCtrl = new GateCtrlDTO();
        gateCtrl.setGateId("G1");
        gateCtrl.setLotId(1L);
        gateCtrl.setIsValid(true);
        gateCtrl.setIsEntry(true);
        gateCtrl.setQrCode("Some QR Code");

        LotOccupancy occupancy = new LotOccupancy();
        occupancy.setLotId(1L);
        occupancy.setCurrentOccupancy(50);
        occupancy.setTimestamp(LocalDateTime.now());

        when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
                .thenReturn(Optional.of(occupancy));
        when(lotOccupancyDB.save(any(LotOccupancy.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        gateResultHandler.receive(gateCtrl);

        // Assert
        verify(gateController, times(1)).gateControl(gateCtrl);
        verify(lotOccupancyDB, times(1)).save(any(LotOccupancy.class));
    }

    /**
     * Verifies that the `GateResultHandler` class can handle a gate control with invalid parking lot id.
     */
    @Test
    void testReceiveInvalidLotId() {
        // Arrange
        GateCtrlDTO gateCtrl = new GateCtrlDTO();
        gateCtrl.setGateId("G1");
        gateCtrl.setLotId(999L);
        gateCtrl.setIsValid(true);
        gateCtrl.setIsEntry(true);

        when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> gateResultHandler.receive(gateCtrl));
    }

    /**
     * Verifies that the `GateResultHandler` class can handle a gate control result with invalid exit.
     */
    @Test
    void testReceiveInvalidExit() {
        // Arrange
        GateCtrlDTO gateCtrl = new GateCtrlDTO();
        gateCtrl.setGateId("G1");
        gateCtrl.setLotId(1L);
        gateCtrl.setIsValid(true);
        gateCtrl.setIsEntry(false);
        gateCtrl.setQrCode("Some QR Code");

        LotOccupancy occupancy = new LotOccupancy();
        occupancy.setLotId(1L);
        occupancy.setCurrentOccupancy(0);
        occupancy.setTimestamp(LocalDateTime.now());

        when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
                .thenReturn(Optional.of(occupancy));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> gateResultHandler.receive(gateCtrl));
    }
}