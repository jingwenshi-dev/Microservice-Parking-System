package ca.mcmaster.cas735.acmepark.payment.business;

import ca.mcmaster.cas735.acmepark.payment.business.entities.ParkingPermits;
import ca.mcmaster.cas735.acmepark.payment.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentRequestProvider;
import ca.mcmaster.cas735.acmepark.payment.ports.required.ParkingPermitsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class PaymentRequestBuilder implements PaymentRequestProvider {

    private final ParkingPermitsRepository permitsRepository;

    @Autowired
    public PaymentRequestBuilder(ParkingPermitsRepository permitsRepository) {
        this.permitsRepository = permitsRepository;
    }

    @Override
    public PaymentRequest buildPaymentRequest(String transponderNumber, LocalDateTime entryTime, LocalDateTime exitTime, BigDecimal hourlyRate) throws NotFoundException {

        // 从数据库获取 ParkingPermits 信息
        ParkingPermits permit = permitsRepository.findByTransponderNumber(transponderNumber).orElseThrow(() -> new NotFoundException("ParkingPermit", transponderNumber, "transponderNumber"));

        // 构造 PaymentRequest
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setLicensePlate(permit.getLicensePlate());
        paymentRequest.setPaymentMethod(permit.getPaymentMethod());
        paymentRequest.setEntryTime(entryTime);
        paymentRequest.setExitTime(exitTime);
        paymentRequest.setHourlyRate(hourlyRate);
        return paymentRequest;

    }

    private PaymentRequest builtRequest(ParkingPermits permit) {
        try {
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setLicensePlate(permit.getLicensePlate());
            paymentRequest.setPaymentMethod(permit.getPaymentMethod());
            paymentRequest.setEntryTime(entryTime);
            paymentRequest.setExitTime(exitTime);
            paymentRequest.setHourlyRate(hourlyRate);
            return paymentRequest;
        } catch (Exception ex) {
            log.error("build payment request error:", ex);
        }

    }
}