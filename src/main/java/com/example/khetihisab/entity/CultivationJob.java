package com.example.khetihisab.entity;

import com.example.khetihisab.enums.JobStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cultivation_jobs", indexes = {
        @Index(name = "idx_job_customer", columnList = "customer_id"),
        @Index(name = "idx_job_driver", columnList = "driver_id"),
        @Index(name = "idx_job_date", columnList = "job_date"),
        @Index(name = "idx_job_customer_date", columnList = "customer_id, job_date")
})
public class CultivationJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "job_date", nullable = false)
    private LocalDate jobDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private JobStatus status = JobStatus.PENDING;

    @Column(length = 500)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    private List<PaymentAllocation> paymentAllocations = new ArrayList<>();

    protected CultivationJob() {
    }

    public CultivationJob(Customer customer, Driver driver, LocalDate jobDate, BigDecimal amount, String notes) {
        this.customer = customer;
        this.driver = driver;
        this.jobDate = jobDate;
        this.amount = amount;
        this.notes = notes;
    }

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public Long getJobId() { return jobId; }
    public Customer getCustomer() { return customer; }
    public Driver getDriver() { return driver; }
    public LocalDate getJobDate() { return jobDate; }
    public BigDecimal getAmount() { return amount; }
    public JobStatus getStatus() { return status; }
    public String getNotes() { return notes; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public void updateDetails(Driver driver, LocalDate jobDate, BigDecimal amount, String notes) {
        this.driver = driver;
        this.jobDate = jobDate;
        this.amount = amount;
        this.notes = notes;
    }

    public void updateStatus(JobStatus status) {
        this.status = status;
    }
}

