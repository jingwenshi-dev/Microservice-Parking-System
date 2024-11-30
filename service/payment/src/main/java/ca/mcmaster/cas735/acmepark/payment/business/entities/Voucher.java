package ca.mcmaster.cas735.acmepark.payment.business.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;


@Data
@Entity
@Table(name = "VOUCHER")
public class Voucher {
    @Id
    private String voucher;

    private LocalDate validFrom;
    private LocalDate validUntil;
}