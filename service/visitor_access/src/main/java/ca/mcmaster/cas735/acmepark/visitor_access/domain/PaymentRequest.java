package ca.mcmaster.cas735.acmepark.visitor_access.domain;

// PaymentRequest 类用于封装支付请求的数据
public class PaymentRequest {
    private String qrCode; // QR 码
    private boolean paymentStatus; // 支付状态

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
