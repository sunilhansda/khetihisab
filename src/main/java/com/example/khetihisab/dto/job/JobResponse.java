package com.example.khetihisab.dto.job;

import com.example.khetihisab.enums.JobStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record JobResponse(Long jobId, Long customerId, String customerName, String addressCode, Long driverId, String driverName, LocalDate jobDate, BigDecimal amount, BigDecimal paidAmount, BigDecimal balance, JobStatus status, String notes) {}
