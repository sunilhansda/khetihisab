package com.example.khetihisab.service;

import com.example.khetihisab.dto.customer.CreateCustomerRequest;
import com.example.khetihisab.dto.customer.CustomerResponse;
import com.example.khetihisab.dto.customer.UpdateCustomerRequest;
import org.springframework.data.domain.Page;

public interface CustomerService {
    CustomerResponse createCustomer(CreateCustomerRequest request);
    CustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request);
    CustomerResponse getCustomer(Long customerId);
    Page<CustomerResponse> searchCustomers(String name, String addressCode, int page, int size);
}
