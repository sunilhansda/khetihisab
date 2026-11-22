package com.example.khetihisab.common.mapper;


import com.example.khetihisab.dto.customer.CustomerResponse;
import com.example.khetihisab.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(customer.getCustomerId(), customer.getName(), customer.getPhoneNumber(), customer.getAddressCode(), customer.getAddress(), customer.getCreatedAt(), customer.getUpdatedAt());
    }
}