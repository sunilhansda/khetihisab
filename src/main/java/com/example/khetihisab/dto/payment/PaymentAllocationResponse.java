package com.example.khetihisab.dto.payment;

import java.math.BigDecimal;

public record PaymentAllocationResponse(Long allocationId, Long jobId, BigDecimal amount) {}
