package com.example.khetihisab.repository;

import com.example.khetihisab.entity.Payment;
import com.example.khetihisab.repository.projection.PaymentSummaryProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(value = """
        SELECT p.payment_id AS paymentId, c.customer_id AS customerId, c.name AS customerName,
               p.payment_date AS paymentDate, p.amount AS amount,
               COALESCE(SUM(a.amount), 0) AS allocatedAmount,
               p.amount - COALESCE(SUM(a.amount), 0) AS unallocatedAmount,
               p.payment_method AS paymentMethod, p.reference_number AS referenceNumber,
               p.notes AS notes, p.created_at AS createdAt
        FROM payments p
        JOIN customers c ON c.customer_id = p.customer_id
        LEFT JOIN payment_allocations a ON a.payment_id = p.payment_id
        WHERE (:customerId IS NULL OR p.customer_id = :customerId)
          AND (:fromDate IS NULL OR p.payment_date >= :fromDate)
          AND (:toDate IS NULL OR p.payment_date <= :toDate)
        GROUP BY p.payment_id, c.customer_id, c.name, p.payment_date, p.amount,
                 p.payment_method, p.reference_number, p.notes, p.created_at
        """,
            countQuery = """
        SELECT COUNT(p.payment_id)
        FROM payments p
        WHERE (:customerId IS NULL OR p.customer_id = :customerId)
          AND (:fromDate IS NULL OR p.payment_date >= :fromDate)
          AND (:toDate IS NULL OR p.payment_date <= :toDate)
        """,
            nativeQuery = true)
    Page<PaymentSummaryProjection> search(@Param("customerId") Long customerId,
                                          @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    @Query(value = """
        SELECT p.payment_id AS paymentId, c.customer_id AS customerId, c.name AS customerName,
               p.payment_date AS paymentDate, p.amount AS amount,
               COALESCE(SUM(a.amount), 0) AS allocatedAmount,
               p.amount - COALESCE(SUM(a.amount), 0) AS unallocatedAmount,
               p.payment_method AS paymentMethod, p.reference_number AS referenceNumber,
               p.notes AS notes, p.created_at AS createdAt
        FROM payments p
        JOIN customers c ON c.customer_id = p.customer_id
        LEFT JOIN payment_allocations a ON a.payment_id = p.payment_id
        WHERE p.payment_id = :paymentId
        GROUP BY p.payment_id, c.customer_id, c.name, p.payment_date, p.amount,
                 p.payment_method, p.reference_number, p.notes, p.created_at
        """, nativeQuery = true)
    Optional<PaymentSummaryProjection> findSummaryByPaymentId(@Param("paymentId") Long paymentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Payment p WHERE p.paymentId = :paymentId")
    Optional<Payment> findByIdForUpdate(@Param("paymentId") Long paymentId);
}
