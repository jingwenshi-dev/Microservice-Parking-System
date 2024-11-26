package ca.mcmaster.cas735.acmepark.payment.business.paymentCalculator;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentCalculatorPort;

import java.math.BigDecimal;


@Component
public class MonthlyPaymentCalculator implements PaymentCalculatorPort {

    @Override
    public BigDecimal calculateParkingFee(PaymentRequest paymentRequest) {
        // 按月计算，忽略具体进出时间
        return new BigDecimal(30);
    }
}