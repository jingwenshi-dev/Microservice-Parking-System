package ca.mcmaster.cas735.acmepark.permit.business.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "Permit")
public class Permit {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int permitId;

    private UUID transponderNumber;

    private LocalDateTime validFrom;

    private LocalDateTime validUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // Assuming the 'Users' table has 'user_id' as the PK
    private User user;

    private Long lotId;

    private String licensePlate;

}
