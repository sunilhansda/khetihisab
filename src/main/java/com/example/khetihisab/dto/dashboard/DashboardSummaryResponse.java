package com.example.khetihisab.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DashboardSummaryResponse(
        LocalDate fromDate,
        LocalDate toDate,
        Long totalJobs,
        Long completedJobs,
        Long pendingJobs,
        BigDecimal totalJobAmount,
        BigDecimal periodReceived,
        BigDecimal outstandingAmount,
        Long activeCustomers,
        Long activeDrivers
) {}
