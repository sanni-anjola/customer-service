package com.example.customerservice.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class BillingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String accountNumber;
    private BigDecimal tariff;

    @PrePersist
    public void updateAccountNumber(){
        this.accountNumber = accountNumber + "-01";
    }
}
