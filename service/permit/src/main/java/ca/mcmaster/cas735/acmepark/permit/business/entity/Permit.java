package ca.mcmaster.cas735.acmepark.permit.business.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Permit")
public class Permit {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int permitId;

    private String transponderNumber;
    private LocalDate validFrom;
    private LocalDate validUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // Assuming the 'Users' table has 'user_id' as the PK
    private User user;

    private int lotId;


    public Permit(String transponderNumber, LocalDate validFrom, LocalDate validUntil,User user, int lotId) {
        this.transponderNumber = transponderNumber;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.user = user;
        this.lotId = lotId;
    }

  // Default constructor for JPA
    public Permit() {

    }

}
