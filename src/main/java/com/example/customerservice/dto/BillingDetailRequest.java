package com.example.customerservice.dto;

import com.example.customerservice.config.PriceConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
public class BillingDetailRequest {

    private String accountNumber;
    @JsonSerialize(using = PriceConfig.class)
    private BigDecimal tariff;
}
