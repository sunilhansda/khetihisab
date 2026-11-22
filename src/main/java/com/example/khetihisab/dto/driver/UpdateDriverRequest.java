package com.example.khetihisab.dto.driver;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDriverRequest(
        @NotBlank(message = "Driver name is required") @Size(max = 100) String name,
        @Size(max = 15) String phoneNumber
) {}