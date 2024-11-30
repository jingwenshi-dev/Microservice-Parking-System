//package ca.mcmaster.cas735.acmepark.payment.business;
//
//import ca.mcmaster.cas735.acmepark.payment.business.entities.ParkingPermits;
//import ca.mcmaster.cas735.acmepark.payment.business.errors.NotFoundException;
//import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
//import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentRequestProvider;
//import ca.mcmaster.cas735.acmepark.payment.ports.required.ParkingPermitsRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
///**
// * 本方法：构造支付请求
// * 查数据库-> 构造支付请求 -> 请求支付
// */
//@Service
//@Slf4j
//public class PaymentRequestBuilder implements PaymentRequestProvider {
//
//    private final ParkingPermitsRepository permitsRepository;
//
//    @Autowired
//    public PaymentRequestBuilder(ParkingPermitsRepository permitsRepository) {
//        this.permitsRepository = permitsRepository;
//    }
//
//    @Override
//    public PaymentRequest buildPaymentRequest(UUID transponderNumber) throws NotFoundException {
//
//        // 从数据库获取 ParkingPermits 信息
//        ParkingPermits permit = permitsRepository.findByTransponderNumber(transponderNumber)
//                .orElseThrow(() -> new NotFoundException("ParkingPermit", transponderNumber.toString(), "transponderNumber"));
//
//        // 构造 PaymentRequest
//
//        return builtRequest(permit);
//    }
//
//    private PaymentRequest builtRequest(ParkingPermits permit) {
//        try {
//            PaymentRequest paymentRequest = new PaymentRequest();
//            paymentRequest.builder()
//                    .transponderNumber(permit.getTransponderNumber())
//                    .validFrom(permit.getValidFrom())
//                    .validUntil(permit.getValidUntil())
//                    .paymentMethod(permit.getPaymentMethod())
//                    .payrollNum(permit.getPayrollNum());
//            return paymentRequest;
//        } catch (Exception ex) {
//            log.error("build payment request error:", ex);
//        }
//        return null;
//    }
//}