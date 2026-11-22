package com.example.khetihisab.dto.job;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateJobRequest(
        @NotNull(message = "Driver ID is required") @Positive Long driverId,
        @NotNull(message = "Job date is required") LocalDate jobDate,
        @NotNull(message = "Amount is required") @Positive(message = "Amount must be greater than zero") BigDecimal amount,
        @Size(max = 500) String notes
) {}