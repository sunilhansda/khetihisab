package com.example.khetihisab.dto.customer;

import java.time.OffsetDateTime;

public record CustomerResponse(Long customerId, String name, String phoneNumber, String addressCode, String address, OffsetDateTime createdAt, OffsetDateTime updatedAt) {}
