package com.example.khetihisab.dto.driver;

import java.time.OffsetDateTime;

public record DriverResponse(Long driverId, String name, String phoneNumber, boolean active, OffsetDateTime createdAt, OffsetDateTime updatedAt) {}
