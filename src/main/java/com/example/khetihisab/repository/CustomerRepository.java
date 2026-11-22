package com.example.khetihisab.repository;

import com.example.khetihisab.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("""
        SELECT c FROM Customer c
        WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:addressCode IS NULL OR UPPER(c.addressCode) = UPPER(:addressCode))
        ORDER BY c.name ASC, c.customerId ASC
        """)
    Page<Customer> search(@Param("name") String name, @Param("addressCode") String addressCode, Pageable pageable);

    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
