package ca.mcmaster.cas735.acmepark.payment.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "USERS")
public class User {
    @Id
    private Long userId;

    private String firstName;
    private String lastName;
    private String email;
    private String userType;
    private BigDecimal totalFine;
}