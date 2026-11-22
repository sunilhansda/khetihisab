package com.example.khetihisab.dto.payment;

import com.example.khetihisab.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record PaymentDetailsResponse(Long paymentId, Long customerId, String customerName, LocalDate paymentDate, BigDecimal amount, BigDecimal allocatedAmount, BigDecimal unallocatedAmount, PaymentMethod paymentMethod, String referenceNumber, String notes, List<PaymentAllocationResponse> allocations, OffsetDateTime createdAt) {}
