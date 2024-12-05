package ca.mcmaster.cas735.acmepark.visitor_access;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import ca.mcmaster.cas735.acmepark.visitor_access.business.*;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.*;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.required.VisitorDataRepository;
import ca.mcmaster.cas735.acmepark.visitor_access.business.entities.Visitor;
import ca.mcmaster.cas735.acmepark.gate.dto.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class ExitReqHandlerImplTests {
    @Mock
    private VisitorSender visitorSender;
    @Mock
    private VisitorDataRepository visitorDataRepository;

    private ExitReqHandlerImpl exitRequestHandler;

    @BeforeEach
    void setUp() {
        exitRequestHandler = new ExitReqHandlerImpl(visitorSender, visitorDataRepository);
    }

    @Test
    void testHandleExit_SendsPaymentRequest() {
        // Arrange
        ValidationDTO validationDTO = new ValidationDTO();
        validationDTO.setTransponderId("transponder1");
        validationDTO.setLicensePlate("ABC123");
        validationDTO.setGateId("Gate1");
        validationDTO.setEntry(true);
        validationDTO.setTimestamp(LocalDateTime.now());
        validationDTO.setLotId(1L);
        validationDTO.setVisitorAllowed(true);
        validationDTO.setHourlyRate(BigDecimal.valueOf(10.0));

        Visitor visitor = new Visitor();
        visitor.setLicensePlate("ABC123");
        visitor.setEntryTime(LocalDateTime.now().minusHours(2));
        visitor.setLotId(1L);

        when(visitorDataRepository.findFirstByLicensePlateOrderByEntryTimeDesc("ABC123"))
                .thenReturn(Optional.of(visitor));

        // Act
        exitRequestHandler.handleExit(validationDTO);

        // Assert
        verify(visitorSender).sendExitRequestToPayment(argThat(request ->
                request.getUserType().equals("visitor") &&
                request.getLicensePlate().equals("ABC123") &&
                request.getLotId().equals(1L) &&
                request.getUserId() == 0 && // Assuming default value
                request.getPermitType() == null && // Assuming default value
                request.getTransponderNumber() == null && // Assuming default value
                request.getValidFrom() == null && // Assuming default value
                request.getValidUntil() == null && // Assuming default value
                request.getPaymentMethod().equals("creditCard") &&
                request.getPayrollNum() == null && // Assuming default value
                request.getAmount() == null && // Assuming default value
                request.getEntryTime().equals(visitor.getEntryTime()) &&
                request.getHourlyRate().equals(BigDecimal.valueOf(10.0)) &&
                !request.isResult() && // Assuming default value
                request.getGateId().equals("Gate1")
        ));
    }
}
