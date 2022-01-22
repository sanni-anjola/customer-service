package com.example.customerservice.dto;

import lombok.Data;


@Data
public class CustomerRequest {
    private String firstName;
    private String lastName;
    private String email;
}
