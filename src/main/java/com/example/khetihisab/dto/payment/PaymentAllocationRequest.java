package com.example.khetihisab.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record PaymentAllocationRequest(
        @NotNull(message = "Job ID is required") @Positive(message = "Job ID must be positive") Long jobId,
        @NotNull(message = "Allocation amount is required") @Positive(message = "Allocation amount must be greater than zero") BigDecimal amount
) {}
