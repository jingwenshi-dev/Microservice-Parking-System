package ca.mcmaster.cas735.acmepark.permit.business.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    private String firstName;

    private String lastName;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private BigDecimal totalOutstandingAmount;

    public enum UserType {
        STUDENT,
        FACULTY,
        STAFF
    }

    public User() {}

    public User(String firstName, String lastName, String email, UserType userType, BigDecimal totalOutstandingAmount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userType = userType;
        this.totalOutstandingAmount = totalOutstandingAmount;
    }

    public UserType getUserType() {
            return userType;
    }

    public int getUserId() {
        return userId;
    }
}
