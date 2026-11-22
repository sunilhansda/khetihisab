package com.example.khetihisab.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(
        @NotBlank(message = "Customer name is required") @Size(max = 100, message = "Name cannot exceed 100 characters") String name,
        @Size(max = 15, message = "Phone number cannot exceed 15 characters") String phoneNumber,
        @Size(max = 20, message = "Address code cannot exceed 20 characters") String addressCode,
        @Size(max = 500, message = "Address cannot exceed 500 characters") String address
) {}
