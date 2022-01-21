package com.example.customerservice.service;

import com.example.customerservice.dto.ApiResponse;
import com.example.customerservice.dto.CustomerRequest;
import com.example.customerservice.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    ApiResponse findAll(Pageable pageable);
    ApiResponse findById(Long id);
    ApiResponse save(CustomerRequest customerRequest);
}
