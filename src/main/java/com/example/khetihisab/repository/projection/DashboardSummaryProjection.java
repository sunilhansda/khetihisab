package com.example.khetihisab.repository.projection;

import java.math.BigDecimal;

public interface DashboardSummaryProjection {
    Long getTotalJobs();
    Long getCompletedJobs();
    Long getPendingJobs();
    BigDecimal getTotalJobAmount();
    BigDecimal getPeriodReceived();
    BigDecimal getOutstandingAmount();
    Long getActiveCustomers();
    Long getActiveDrivers();
}
