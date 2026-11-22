package com.example.khetihisab.service.impl;

import com.example.khetihisab.common.exception.BusinessRuleException;
import com.example.khetihisab.common.exception.ResourceNotFoundException;
import com.example.khetihisab.common.mapper.CultivationJobMapper;
import com.example.khetihisab.common.util.PageRequestUtil;
import com.example.khetihisab.dto.job.CreateJobRequest;
import com.example.khetihisab.dto.job.JobResponse;
import com.example.khetihisab.dto.job.UpdateJobRequest;
import com.example.khetihisab.dto.job.UpdateJobStatusRequest;
import com.example.khetihisab.entity.CultivationJob;
import com.example.khetihisab.entity.Customer;
import com.example.khetihisab.entity.Driver;
import com.example.khetihisab.enums.JobStatus;
import com.example.khetihisab.repository.*;
import com.example.khetihisab.service.CultivationJobService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@Transactional
public class CultivationJobServiceImpl implements CultivationJobService {
    private final CultivationJobRepository jobRepository;
    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;
    private final PaymentAllocationRepository allocationRepository;
    private final CultivationJobMapper jobMapper;

    public CultivationJobServiceImpl(CultivationJobRepository jobRepository, CustomerRepository customerRepository, DriverRepository driverRepository, PaymentAllocationRepository allocationRepository, CultivationJobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
        this.allocationRepository = allocationRepository;
        this.jobMapper = jobMapper;
    }

    @Override
    public JobResponse createJob(CreateJobRequest request) {
        validateDateRange(request.jobDate(), request.jobDate());
        Customer customer = customerRepository.findById(request.customerId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.customerId()));
        Driver driver = getActiveDriver(request.driverId());
        CultivationJob job = new CultivationJob(customer, driver, request.jobDate(), request.amount(), request.notes());
        CultivationJob saved = jobRepository.saveAndFlush(job);
        return getJob(saved.getJobId());
    }

    @Override
    public JobResponse updateJob(Long jobId, UpdateJobRequest request) {
        CultivationJob job = jobRepository.findByIdForUpdate(jobId).orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        Driver driver = getActiveDriver(request.driverId());
        if (allocationRepository.existsByJob_JobId(jobId) && job.getAmount().compareTo(request.amount()) != 0) {
            throw new BusinessRuleException("Job amount cannot be changed because payments have already been allocated");
        }
        job.updateDetails(driver, request.jobDate(), request.amount(), request.notes());
        jobRepository.flush();
        return getJob(jobId);
    }

    @Override
    public JobResponse updateJobStatus(Long jobId, UpdateJobStatusRequest request) {
        CultivationJob job = jobRepository.findByIdForUpdate(jobId).orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        if (request.status() == JobStatus.CANCELLED && allocationRepository.existsByJob_JobId(jobId)) {
            throw new BusinessRuleException("A job with allocated payments cannot be cancelled");
        }
        job.updateStatus(request.status());
        jobRepository.flush();
        return getJob(jobId);
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getJob(Long jobId) {
        return jobRepository.findSummaryById(jobId).map(jobMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobResponse> searchJobs(Long customerId, Long driverId, JobStatus status, LocalDate fromDate, LocalDate toDate, int page, int size) {
        validateDateRange(fromDate, toDate);
        return jobRepository.search(customerId, driverId, status, fromDate, toDate, PageRequestUtil.create(page, size)).map(jobMapper::toResponse);
    }

    private Driver getActiveDriver(Long driverId) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + driverId));
        if (!driver.isActive()) throw new BusinessRuleException("Cannot assign an inactive driver to a job");
        return driver;
    }

    private void validateDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) throw new BusinessRuleException("From date cannot be after to date");
    }
}
