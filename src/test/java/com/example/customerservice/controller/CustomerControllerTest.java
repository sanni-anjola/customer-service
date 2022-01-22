package com.example.customerservice.controller;

import com.example.customerservice.dto.ApiResponse;
import com.example.customerservice.dto.CustomerRequest;
import com.example.customerservice.model.Customer;
import com.example.customerservice.service.BillingDetailService;
import com.example.customerservice.service.CustomerService;
import com.example.customerservice.util.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private CustomerController controller;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private BillingDetailService billingDetailService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void getAllCustomer() throws Exception {
        Customer customer1 = Customer
                .builder()
                .firstName("First Name")
                .lastName("Last name")
                .email("first@gmail.com")
                .build();

        Customer customer2 = Customer
                .builder()
                .firstName("First Name")
                .lastName("Last name")
                .email("last@gmail.com")
                .build();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus("success");
        apiResponse.getData().put("customers", List.of(customer1, customer2));
        apiResponse.getData().put("results", 2);
        apiResponse.getData().put("TotalNumberOfCustomers", 2);
        apiResponse.getData().put("pageNumber", 1);
        apiResponse.getData().put("TotalPages", 1);
        doReturn(apiResponse).when(customerService).findAll(1, 10);

        this.mockMvc.perform(get("/api/v1/"))
                .andDo(print())
                // test status
                .andExpect(status().isOk())
                // test contentType
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // test returned response
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.pageNumber", is(1)))
                .andExpect(jsonPath("$.data.TotalNumberOfCustomers", is(2)))
                .andExpect(jsonPath("$.data.customers", hasSize(2)))
                .andExpect(jsonPath("$.data.customers[0].firstName", is("First Name")))
                .andExpect(jsonPath("$.data.customers[0].email", is("first@gmail.com")))
                .andExpect(jsonPath("$.data.customers[1].firstName", is("First Name")))
                .andExpect(jsonPath("$.data.customers[1].email", is("last@gmail.com")));


        doReturn(apiResponse).when(customerService).findAll(1, 5);

        this.mockMvc.perform(get("/api/v1/?page=1&size=5"))
                .andDo(print())
                // test status
                .andExpect(status().isOk())
                // test contentType
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // test returned response
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.pageNumber", is(1)))
                .andExpect(jsonPath("$.data.TotalNumberOfCustomers", is(2)))
                .andExpect(jsonPath("$.data.customers", hasSize(2)))
                .andExpect(jsonPath("$.data.customers[0].firstName", is("First Name")))
                .andExpect(jsonPath("$.data.customers[0].email", is("first@gmail.com")))
                .andExpect(jsonPath("$.data.customers[1].firstName", is("First Name")))
                .andExpect(jsonPath("$.data.customers[1].email", is("last@gmail.com")));

    }

    @Test
    void getAllCustomerThrowsError() throws Exception {

        doThrow(ApplicationException.class).when(customerService).findAll(-1, 5);
        this.mockMvc.perform(get("/api/v1/?page=-1&size=5"))
                .andDo(print())
                // test status
                .andExpect(status().isBadRequest())
                // test contentType
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // test resolved exception
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApplicationException));
    }

    @Test
    void saveCustomer() throws Exception {
        Customer customer1 = Customer
                .builder()
                .firstName("First Name")
                .lastName("Last name")
                .email("first@gmail.com")
                .build();

        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setFirstName("First Name");
        customerRequest.setLastName("Last Name");
        customerRequest.setEmail("first@gmail.com");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus("success");
        apiResponse.getData().put("customer", customer1);
        doReturn(apiResponse).when(customerService).save(customerRequest);
        this.mockMvc.perform(post("/api/v1/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"First Name\", \"lastName\": \"Last Name\", \"email\": \"first@gmail.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())

                // test status
                .andExpect(status().isCreated())
                // test contentType
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // test returned response
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.customer.firstName", is("First Name")))
                .andExpect(jsonPath("$.data.customer.email", is("first@gmail.com")));
    }

    @Test
    void saveCustomerThrowsError() throws Exception {


        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setFirstName("First Name");
        customerRequest.setLastName("Last Name");
        customerRequest.setEmail("first@gmail");
        doThrow(ConstraintViolationException.class).when(customerService).save(customerRequest);
        this.mockMvc.perform(post("/api/v1/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"First Name\", \"lastName\": \"Last Name\", \"email\": \"first@gmail\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())

                // test status
                .andExpect(status().isBadRequest())
                // test resolved exception
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
    }


    @Test
    void getCustomer() {
    }

    @Test
    void getCustomerBillingDetails() {
    }

    @Test
    void addBillingToCustomer() {
    }
}