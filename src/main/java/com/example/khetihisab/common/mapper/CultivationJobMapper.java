package com.example.khetihisab.common.mapper;

import java.math.BigDecimal;

import com.example.khetihisab.dto.job.JobResponse;
import com.example.khetihisab.repository.JobSummaryProjection;
import org.springframework.stereotype.Component;

@Component
public class CultivationJobMapper {
    public JobResponse toResponse(JobSummaryProjection job) {
        return new JobResponse(job.getJobId(), job.getCustomerId(), job.getCustomerName(), job.getAddressCode(), job.getDriverId(), job.getDriverName(), job.getJobDate(), job.getAmount(), job.getPaidAmount(), job.getBalance(), job.getStatus(), job.getNotes());
    }
}
