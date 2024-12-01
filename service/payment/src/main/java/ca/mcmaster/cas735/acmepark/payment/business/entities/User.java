package ca.mcmaster.cas735.acmepark.payment.business.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自增主键
    private Long userId;

    private String firstName;
    private String lastName;
    private String email;
    private String userType;
    private BigDecimal totalFine;
}