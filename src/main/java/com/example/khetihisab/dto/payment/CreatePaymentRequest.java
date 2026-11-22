package com.example.khetihisab.dto.payment;

import com.example.khetihisab.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreatePaymentRequest(
        @NotNull(message = "Customer ID is required") @Positive(message = "Customer ID must be positive") Long customerId,
        @NotNull(message = "Payment date is required") LocalDate paymentDate,
        @NotNull(message = "Payment amount is required") @Positive(message = "Payment amount must be greater than zero") BigDecimal amount,
        @NotNull(message = "Payment method is required") PaymentMethod paymentMethod,
        @Size(max = 100) String referenceNumber,
        @Size(max = 500) String notes,
        @Valid List<PaymentAllocationRequest> allocations
) {}
