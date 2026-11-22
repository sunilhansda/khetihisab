package com.example.khetihisab.repository;

import com.example.khetihisab.entity.CultivationJob;
import com.example.khetihisab.enums.JobStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface CultivationJobRepository extends JpaRepository<CultivationJob, Long> {

    @Query(
            value = """
                    SELECT
                        j.jobId AS jobId,
                        c.customerId AS customerId,
                        c.name AS customerName,
                        c.addressCode AS addressCode,
                        d.driverId AS driverId,
                        d.name AS driverName,
                        j.jobDate AS jobDate,
                        j.amount AS amount,
                        COALESCE(SUM(a.amount), 0) AS paidAmount,
                        j.amount - COALESCE(SUM(a.amount), 0) AS balance,
                        j.status AS status,
                        j.notes AS notes
                    FROM CultivationJob j
                    JOIN j.customer c
                    JOIN j.driver d
                    LEFT JOIN j.paymentAllocations a
                    WHERE (:customerId IS NULL OR c.customerId = :customerId)
                      AND (:driverId IS NULL OR d.driverId = :driverId)
                      AND (:status IS NULL OR j.status = :status)
                      AND (:fromDate IS NULL OR j.jobDate >= :fromDate)
                      AND (:toDate IS NULL OR j.jobDate <= :toDate)
                    GROUP BY
                        j.jobId, c.customerId, c.name, c.addressCode,
                        d.driverId, d.name, j.jobDate, j.amount, j.status, j.notes
                    ORDER BY j.jobDate DESC, j.jobId DESC
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT j.jobId)
                    FROM CultivationJob j
                    WHERE (:customerId IS NULL OR j.customer.customerId = :customerId)
                      AND (:driverId IS NULL OR j.driver.driverId = :driverId)
                      AND (:status IS NULL OR j.status = :status)
                      AND (:fromDate IS NULL OR j.jobDate >= :fromDate)
                      AND (:toDate IS NULL OR j.jobDate <= :toDate)
                    """
    )
    Page<JobSummaryProjection> search(
            @Param("customerId") Long customerId,
            @Param("driverId") Long driverId,
            @Param("status") JobStatus status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT j FROM CultivationJob j WHERE j.jobId = :jobId")
    Optional<CultivationJob> findByIdForUpdate(@Param("jobId") Long jobId);

    @Query(
            value = """
                    SELECT
                        j.jobId AS jobId,
                        c.customerId AS customerId,
                        c.name AS customerName,
                        c.addressCode AS addressCode,
                        d.driverId AS driverId,
                        d.name AS driverName,
                        j.jobDate AS jobDate,
                        j.amount AS amount,
                        COALESCE(SUM(a.amount), 0) AS paidAmount,
                        j.amount - COALESCE(SUM(a.amount), 0) AS balance,
                        j.status AS status,
                        j.notes AS notes
                    FROM CultivationJob j
                    JOIN j.customer c
                    JOIN j.driver d
                    LEFT JOIN j.paymentAllocations a
                    WHERE j.jobId = :jobId
                    GROUP BY
                        j.jobId, c.customerId, c.name, c.addressCode,
                        d.driverId, d.name, j.jobDate, j.amount, j.status, j.notes
                    """
    )
    Optional<JobSummaryProjection> findSummaryById(@Param("jobId") Long jobId);
}
