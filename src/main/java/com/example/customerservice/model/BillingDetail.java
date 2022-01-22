package com.example.customerservice.model;

import com.example.customerservice.config.PriceConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Entity
public class BillingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Pattern(regexp = "[0-9]{10}", message = "Account number can only be numbers")
    @Size(min = 10, max = 10, message = "Account number can only be of length ten")
    @NotNull(message = "Account number cannot be null")
    private String accountNumber;

    @JsonSerialize(using = PriceConfig.class)
    private BigDecimal tariff;

    @PrePersist
    public void updateAccountNumber(){
        this.accountNumber = accountNumber + "-01";
    }
}
