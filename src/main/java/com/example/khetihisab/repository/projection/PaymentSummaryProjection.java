package com.example.khetihisab.repository.projection;

import com.example.khetihisab.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public interface PaymentSummaryProjection {
    Long getPaymentId();
    Long getCustomerId();
    String getCustomerName();
    LocalDate getPaymentDate();
    BigDecimal getAmount();
    BigDecimal getAllocatedAmount();
    BigDecimal getUnallocatedAmount();
    PaymentMethod getPaymentMethod();
    String getReferenceNumber();
    String getNotes();
    OffsetDateTime getCreatedAt();
}
