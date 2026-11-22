package com.example.khetihisab.repository;

import com.example.khetihisab.enums.JobStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface JobSummaryProjection {
    Long getJobId();
    Long getCustomerId();
    String getCustomerName();
    String getAddressCode();
    Long getDriverId();
    String getDriverName();
    LocalDate getJobDate();
    BigDecimal getAmount();
    BigDecimal getPaidAmount();
    BigDecimal getBalance();
    JobStatus getStatus();
    String getNotes();
}
