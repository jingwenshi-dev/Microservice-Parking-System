package ca.mcmaster.cas735.acmepark.visitor_access.business.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "VISITOR")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    private Long id;
    private String licensePlate;
    private LocalDateTime entryTime;
    private Long lotId;
}