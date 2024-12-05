package ca.mcmaster.cas735.acmepark.gate;

import ca.mcmaster.cas735.acmepark.gate.business.Dashboard;
import ca.mcmaster.cas735.acmepark.gate.business.GateResultHandler;
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

    private GateCtrlDTO createTestGateCtrlDTO(Long lotId, boolean isEntry) {
        GateCtrlDTO gateCtrl = new GateCtrlDTO();
        gateCtrl.setGateId("G1");
        gateCtrl.setLotId(lotId);
        gateCtrl.setIsValid(true);
        gateCtrl.setIsEntry(isEntry);
        gateCtrl.setQrCode("Some QR Code");
        return gateCtrl;
    }

    private LotOccupancy createTestLotOccupancy(int currentOccupancy) {
        LotOccupancy occupancy = new LotOccupancy();
        occupancy.setLotId(1L);
        occupancy.setCurrentOccupancy(currentOccupancy);
        occupancy.setTimestamp(LocalDateTime.now());
        return occupancy;
    }

    @Test
    void testReceiveValidResult() throws NotFoundException {
        // Arrange
        GateCtrlDTO gateCtrl = createTestGateCtrlDTO(1L, true);
        LotOccupancy occupancy = createTestLotOccupancy(50);

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

    @Test
    void testReceiveInvalidLotId() {
        // Arrange
        GateCtrlDTO gateCtrl = createTestGateCtrlDTO(999L, true);

        when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> gateResultHandler.receive(gateCtrl));
    }

    @Test
    void testReceiveInvalidExit() {
        // Arrange
        GateCtrlDTO gateCtrl = createTestGateCtrlDTO(1L, false);
        LotOccupancy occupancy = createTestLotOccupancy(0);

        when(lotOccupancyDB.findFirstByLotIdOrderByTimestampDesc(1L))
                .thenReturn(Optional.of(occupancy));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> gateResultHandler.receive(gateCtrl));
    }
}