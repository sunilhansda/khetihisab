package com.example.khetihisab.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payment_allocations", indexes = {
        @Index(name = "idx_allocation_job", columnList = "job_id"),
        @Index(name = "idx_allocation_payment", columnList = "payment_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_payment_job", columnNames = {"payment_id", "job_id"})
})
public class PaymentAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allocation_id")
    private Long allocationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private CultivationJob job;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected PaymentAllocation() {
    }

    public PaymentAllocation(Payment payment, CultivationJob job, BigDecimal amount) {
        this.payment = payment;
        this.job = job;
        this.amount = amount;
    }

    @PrePersist
    void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    public Long getAllocationId() { return allocationId; }
    public Payment getPayment() { return payment; }
    public CultivationJob getJob() { return job; }
    public BigDecimal getAmount() { return amount; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}

