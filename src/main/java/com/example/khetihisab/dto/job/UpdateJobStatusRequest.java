package com.example.khetihisab.dto.job;

import com.example.khetihisab.enums.JobStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateJobStatusRequest(@NotNull(message = "Job status is required") JobStatus status) {}
