package com.example.customerservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class CustomerRequest {
    private String firstName;
    private String lastName;
    @Email
    private String email;
}
