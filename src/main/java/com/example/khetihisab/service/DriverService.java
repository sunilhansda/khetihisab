package com.example.khetihisab.service;

import com.example.khetihisab.dto.driver.CreateDriverRequest;
import com.example.khetihisab.dto.driver.DriverResponse;
import com.example.khetihisab.dto.driver.UpdateDriverRequest;
import org.springframework.data.domain.Page;

public interface DriverService {
    DriverResponse createDriver(CreateDriverRequest request);
    DriverResponse updateDriver(Long driverId, UpdateDriverRequest request);
    DriverResponse getDriver(Long driverId);
    Page<DriverResponse> searchDrivers(String name, Boolean active, int page, int size);
    DriverResponse activateDriver(Long driverId);
    DriverResponse deactivateDriver(Long driverId);
}
