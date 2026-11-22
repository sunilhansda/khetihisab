package com.example.khetihisab.repository;

import com.example.khetihisab.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    @Query("""
        SELECT d FROM Driver d
        WHERE (:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:active IS NULL OR d.active = :active)
        ORDER BY d.name ASC, d.driverId ASC
        """)
    Page<Driver> search(@Param("name") String name, @Param("active") Boolean active, Pageable pageable);
}
