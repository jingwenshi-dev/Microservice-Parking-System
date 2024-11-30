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

// TODO: 这个代码放在哪个地方呢
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

        // 将生成的二维码数据转换为 Base64 字符串以便通过消息传递
        return Base64.getEncoder().encodeToString(pngData);
    }

    @Override
    public String readQRCode(String base64QRCode) throws Exception {
        // 将 Base64 编码的二维码数据解码为字节数组
        byte[] decodedBytes = Base64.getDecoder().decode(base64QRCode);

        // 将字节数组转换为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        // 使用 ZXing 读取二维码内容
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);

        // 返回读取到的车牌号信息
        return result.getText();
    }
}
