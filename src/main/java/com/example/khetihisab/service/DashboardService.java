package com.example.khetihisab.service;

import com.example.khetihisab.dto.dashboard.DashboardSummaryResponse;

import java.time.LocalDate;

public interface DashboardService {
    DashboardSummaryResponse getSummary(LocalDate fromDate, LocalDate toDate);
}
