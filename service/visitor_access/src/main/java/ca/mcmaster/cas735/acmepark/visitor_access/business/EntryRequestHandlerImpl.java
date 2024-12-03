package ca.mcmaster.cas735.acmepark.visitor_access.business;

import ca.mcmaster.cas735.acmepark.gate.dto.GateCtrlDTO;
import ca.mcmaster.cas735.acmepark.gate.dto.ValidationDTO;
import ca.mcmaster.cas735.acmepark.visitor_access.business.entities.Visitor;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.QRCodeService;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.VisitorSender;
import ca.mcmaster.cas735.acmepark.visitor_access.ports.required.VisitorDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EntryRequestHandlerImpl implements EntryRequestHandler {

    private final VisitorSender visitorSender;
    private final QRCodeService qrCodeService;
    private final VisitorDataRepository visitorDataRepository;


    @Autowired
    public EntryRequestHandlerImpl(VisitorSender visitorSender, QRCodeService qrCodeService,
                                   VisitorDataRepository visitorDataRepository) {
        this.visitorSender = visitorSender;
        this.qrCodeService = qrCodeService;
        this.visitorDataRepository = visitorDataRepository;
    }

    @Override
    public void handleEntry(ValidationDTO validationDTO) {
        GateCtrlDTO gateCtrlDTO = new GateCtrlDTO();
        if (validationDTO != null && validationDTO.isVisitorAllowed()) {
            //写入数据库进入时间。
            setVisitorToRepository(validationDTO);
            gateCtrlDTO.setIsValid(true);
            gateCtrlDTO.setGateId(validationDTO.getGateId());
            gateCtrlDTO.setLotId(validationDTO.getLotId());
            gateCtrlDTO.setIsEntry(validationDTO.isEntry());
            // 添加QR数据
            addQRCode(validationDTO, gateCtrlDTO);
        } else {
            gateCtrlDTO.setIsValid(false);
        }
        visitorSender.sendEntryResponseToGate(gateCtrlDTO);
    }

    private void setVisitorToRepository(ValidationDTO validationDTO) {
        try {
            Visitor newVisitor = new Visitor();
            newVisitor.setLicensePlate(validationDTO.getLicensePlate());
            newVisitor.setEntryTime(LocalDateTime.now()); // 设置进入时间
            visitorDataRepository.save(newVisitor); // 保存新的访客
            log.info("New visitor created with LicensePlate: {}, EntryTime: {}", newVisitor.getLicensePlate(), newVisitor.getEntryTime());
        } catch (Exception ex) {
            log.error("add new user error:", ex);
        }
    }

    // 将原始 JSON 数据转换为 QR 码字符串
    private void addQRCode(ValidationDTO validationDTO, GateCtrlDTO gateCtrlDTO) {
        try {
            if (validationDTO != null &&
                    validationDTO.isEntry() && StringUtils.hasLength(validationDTO.getLicensePlate())) {
                String qrCode = qrCodeService.generateQRCode(validationDTO.getLicensePlate());
                gateCtrlDTO.setQrCode(qrCode);
            }
        } catch (Exception e) {
            log.error("some error in translate:", e);
        }
    }

}
