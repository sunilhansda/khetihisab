package com.example.khetihisab.repository;

import com.example.khetihisab.entity.PaymentAllocation;
import com.example.khetihisab.repository.projection.PaymentAllocationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface PaymentAllocationRepository extends JpaRepository<PaymentAllocation, Long> {
    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM PaymentAllocation a WHERE a.payment.paymentId = :paymentId")
    BigDecimal getAllocatedAmountByPaymentId(@Param("paymentId") Long paymentId);

    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM PaymentAllocation a WHERE a.job.jobId = :jobId")
    BigDecimal getAllocatedAmountByJobId(@Param("jobId") Long jobId);

    @Query("""
        SELECT a.allocationId AS allocationId, a.job.jobId AS jobId, a.amount AS amount
        FROM PaymentAllocation a
        WHERE a.payment.paymentId = :paymentId
        ORDER BY a.allocationId
        """)
    List<PaymentAllocationProjection> findDetailsByPaymentId(@Param("paymentId") Long paymentId);

    boolean existsByJob_JobId(Long jobId);
}
