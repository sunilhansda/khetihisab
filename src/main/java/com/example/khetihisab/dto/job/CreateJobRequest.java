package com.example.khetihisab.dto.job;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateJobRequest(
        @NotNull(message = "Customer ID is required") @Positive(message = "Customer ID must be positive") Long customerId,
        @NotNull(message = "Driver ID is required") @Positive(message = "Driver ID must be positive") Long driverId,
        @NotNull(message = "Job date is required") LocalDate jobDate,
        @NotNull(message = "Amount is required") @Positive(message = "Amount must be greater than zero") BigDecimal amount,
        @Size(max = 500, message = "Notes cannot exceed 500 characters") String notes
) {}
