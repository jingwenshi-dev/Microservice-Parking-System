package ca.mcmaster.cas735.acmepark.payment.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "VISITOR")
public class Visitor {
    @Id
    private String licensePlate;

    private String voucher;
    private LocalDateTime entryTime;
    private Long lotId;
}