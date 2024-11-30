package ca.mcmaster.cas735.acmepark.visitor_access.ports.provided;

public interface QRCodeService {

    String generateQRCode(String data) throws Exception;

    String readQRCode(String data) throws Exception;
}
