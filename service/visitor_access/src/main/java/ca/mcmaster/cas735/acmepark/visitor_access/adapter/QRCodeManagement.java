package ca.mcmaster.cas735.acmepark.visitor_access.adapter;

import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.QRCodeService;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Component;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Component
public class QRCodeManagement implements QRCodeService {
    @Override
    public String generateQRCode(String data) throws Exception {
        int width = 300;
        int height = 300;
        BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        // Converts the generated QR code data into a Base64 string for passing via message
        return Base64.getEncoder().encodeToString(pngData);
    }

    @Override
    public String readQRCode(String base64QRCode) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(base64QRCode);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }
}
