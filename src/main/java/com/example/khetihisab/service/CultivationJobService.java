package com.example.khetihisab.service;

import com.example.khetihisab.dto.job.CreateJobRequest;
import com.example.khetihisab.dto.job.JobResponse;
import com.example.khetihisab.dto.job.UpdateJobRequest;
import com.example.khetihisab.dto.job.UpdateJobStatusRequest;
import com.example.khetihisab.enums.JobStatus;
import org.springframework.data.domain.Page;
import java.time.LocalDate;

public interface CultivationJobService {
    JobResponse createJob(CreateJobRequest request);
    JobResponse updateJob(Long jobId, UpdateJobRequest request);
    JobResponse updateJobStatus(Long jobId, UpdateJobStatusRequest request);
    JobResponse getJob(Long jobId);
    Page<JobResponse> searchJobs(Long customerId, Long driverId, JobStatus status, LocalDate fromDate, LocalDate toDate, int page, int size);
}