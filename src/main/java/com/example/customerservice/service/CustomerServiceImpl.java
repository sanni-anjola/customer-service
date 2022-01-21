package com.example.customerservice.service;

import com.example.customerservice.dto.ApiResponse;
import com.example.customerservice.dto.CustomerRequest;
import com.example.customerservice.model.Customer;
import com.example.customerservice.repository.CustomerRepository;
import com.example.customerservice.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public ApiResponse findAll(Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus("success");
        apiResponse.getResponse().put("data", customerRepository.findAll(pageable));
        return apiResponse;
    }

    @Override
    public ApiResponse findById(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        Customer customer = optionalCustomer.orElseThrow(() -> new NotFoundException("Customer", "id", String.valueOf(id)));
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus("success");
        apiResponse.getResponse().put("data", customer);
        return apiResponse;
    }

    @Override
    public ApiResponse save(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setEmail(customerRequest.getEmail());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus("success");
        Customer savedCustomer = customerRepository.save(customer);
        apiResponse.getResponse().put("data", savedCustomer);
        return apiResponse;
    }
}
