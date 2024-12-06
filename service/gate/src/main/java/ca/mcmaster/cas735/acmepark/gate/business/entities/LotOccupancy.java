package ca.mcmaster.cas735.acmepark.gate.business.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "LOT_OCCUPANCY")
public class LotOccupancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lotId;

    private LocalDateTime timestamp;

    private int currentOccupancy;
}