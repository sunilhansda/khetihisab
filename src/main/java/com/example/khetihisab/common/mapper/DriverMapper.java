package com.example.khetihisab.common.mapper;

import com.example.khetihisab.dto.driver.DriverResponse;
import com.example.khetihisab.entity.Driver;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {
    public DriverResponse toResponse(Driver driver) {
        return new DriverResponse(driver.getDriverId(), driver.getName(), driver.getPhoneNumber(), driver.isActive(), driver.getCreatedAt(), driver.getUpdatedAt());
    }
}
