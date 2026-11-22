package com.example.khetihisab.repository;

import com.example.khetihisab.entity.CultivationJob;
import com.example.khetihisab.repository.projection.DashboardSummaryProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface DashboardRepository extends Repository<CultivationJob, Long> {

    @Query(value = """
            SELECT
                (SELECT COUNT(*)
                 FROM cultivation_jobs j
                 WHERE (:fromDate IS NULL OR j.job_date >= :fromDate)
                   AND (:toDate IS NULL OR j.job_date <= :toDate)
                ) AS "totalJobs",

                (SELECT COUNT(*)
                 FROM cultivation_jobs j
                 WHERE j.status = 'COMPLETED'
                   AND (:fromDate IS NULL OR j.job_date >= :fromDate)
                   AND (:toDate IS NULL OR j.job_date <= :toDate)
                ) AS "completedJobs",

                (SELECT COUNT(*)
                 FROM cultivation_jobs j
                 WHERE j.status = 'PENDING'
                   AND (:fromDate IS NULL OR j.job_date >= :fromDate)
                   AND (:toDate IS NULL OR j.job_date <= :toDate)
                ) AS "pendingJobs",

                (SELECT COALESCE(SUM(j.amount), 0)
                 FROM cultivation_jobs j
                 WHERE (:fromDate IS NULL OR j.job_date >= :fromDate)
                   AND (:toDate IS NULL OR j.job_date <= :toDate)
                ) AS "totalJobAmount",

                (SELECT COALESCE(SUM(p.amount), 0)
                 FROM payments p
                 WHERE (:fromDate IS NULL OR p.payment_date >= :fromDate)
                   AND (:toDate IS NULL OR p.payment_date <= :toDate)
                ) AS "periodReceived",

                (
                    (SELECT COALESCE(SUM(j.amount), 0)
                     FROM cultivation_jobs j
                     WHERE j.status <> 'CANCELLED')
                    -
                    (SELECT COALESCE(SUM(a.amount), 0)
                     FROM payment_allocations a
                     JOIN cultivation_jobs j ON j.job_id = a.job_id
                     WHERE j.status <> 'CANCELLED')
                ) AS "outstandingAmount",

                (SELECT COUNT(DISTINCT j.customer_id)
                 FROM cultivation_jobs j
                 WHERE j.status <> 'CANCELLED'
                   AND (:fromDate IS NULL OR j.job_date >= :fromDate)
                   AND (:toDate IS NULL OR j.job_date <= :toDate)
                ) AS "activeCustomers",

                (SELECT COUNT(*)
                 FROM drivers d
                 WHERE d.is_active = TRUE
                ) AS "activeDrivers"
            """, nativeQuery = true)
    Optional<DashboardSummaryProjection> getDashboardSummary(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}
