package ca.mcmaster.cas735.acmepark.payment.business.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "LOT_OCCUPANCY")
public class LotOccupancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自增主键
    private Long id;

    private Long lotId;

    private LocalDateTime timestamp;
    private int currentOccupancy;
}