package com.example.khetihisab.service.impl;

import com.example.khetihisab.common.exception.BusinessRuleException;
import com.example.khetihisab.dto.dashboard.DashboardSummaryResponse;
import com.example.khetihisab.repository.DashboardRepository;
import com.example.khetihisab.repository.projection.DashboardSummaryProjection;
import com.example.khetihisab.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository dashboardRepository;

    public DashboardServiceImpl(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @Override
    public DashboardSummaryResponse getSummary(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new BusinessRuleException("From date cannot be after to date");
        }
        DashboardSummaryProjection result = dashboardRepository.getSummary(fromDate, toDate)
                .orElseThrow(() -> new IllegalStateException("Dashboard summary could not be calculated"));
        return new DashboardSummaryResponse(fromDate, toDate, result.getTotalJobs(), result.getCompletedJobs(),
                result.getPendingJobs(), result.getTotalJobAmount(), result.getPeriodReceived(),
                result.getOutstandingAmount(), result.getActiveCustomers(), result.getActiveDrivers());
    }
}
