package com.example.khetihisab.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCustomerRequest(
        @NotBlank(message = "Customer name is required") @Size(max = 100) String name,
        @Size(max = 15) String phoneNumber,
        @Size(max = 20) String addressCode,
        @Size(max = 500) String address
) {}