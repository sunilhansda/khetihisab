package com.example.khetihisab.controller;

import com.example.khetihisab.dto.job.CreateJobRequest;
import com.example.khetihisab.dto.job.JobResponse;
import com.example.khetihisab.dto.job.UpdateJobRequest;
import com.example.khetihisab.dto.job.UpdateJobStatusRequest;
import com.example.khetihisab.enums.JobStatus;
import com.example.khetihisab.service.CultivationJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/jobs")
@Tag(name = "Cultivation Jobs", description = "Cultivation job management APIs")
public class CultivationJobController {

    private final CultivationJobService cultivationJobService;

    public CultivationJobController(CultivationJobService cultivationJobService) {
        this.cultivationJobService = cultivationJobService;
    }

    @PostMapping
    @Operation(summary = "Create a cultivation job")
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody CreateJobRequest request) {
        JobResponse response = cultivationJobService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{jobId}")
    @Operation(summary = "Update a cultivation job")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long jobId,
            @Valid @RequestBody UpdateJobRequest request) {
        return ResponseEntity.ok(cultivationJobService.updateJob(jobId, request));
    }

    @PatchMapping("/{jobId}/status")
    @Operation(summary = "Update cultivation job status")
    public ResponseEntity<JobResponse> updateJobStatus(
            @PathVariable Long jobId,
            @Valid @RequestBody UpdateJobStatusRequest request) {
        return ResponseEntity.ok(cultivationJobService.updateJobStatus(jobId, request));
    }

    @GetMapping("/{jobId}")
    @Operation(summary = "Get cultivation job by ID")
    public ResponseEntity<JobResponse> getJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(cultivationJobService.getJob(jobId));
    }

    @GetMapping
    @Operation(summary = "Search cultivation jobs with filters and pagination")
    public ResponseEntity<Page<JobResponse>> searchJobs(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long driverId,
            @RequestParam(required = false) JobStatus status,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(cultivationJobService.searchJobs(
                customerId, driverId, status, fromDate, toDate, page, size));
    }
}

