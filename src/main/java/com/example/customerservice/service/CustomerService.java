package com.example.customerservice.service;

import com.example.customerservice.dto.ApiResponse;
import com.example.customerservice.dto.CustomerRequest;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CustomerService {
    ApiResponse findAll(int page, int size);
    ApiResponse findById(Long id);
    ApiResponse save(CustomerRequest customerRequest);
}
