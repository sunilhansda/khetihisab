package com.example.khetihisab.service.impl;

import com.example.khetihisab.common.exception.ResourceNotFoundException;
import com.example.khetihisab.common.mapper.DriverMapper;
import com.example.khetihisab.common.util.PageRequestUtil;
import com.example.khetihisab.dto.driver.CreateDriverRequest;
import com.example.khetihisab.dto.driver.DriverResponse;
import com.example.khetihisab.dto.driver.UpdateDriverRequest;
import com.example.khetihisab.entity.Driver;
import com.example.khetihisab.repository.DriverRepository;
import com.example.khetihisab.service.DriverService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public DriverServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    @Override
    public DriverResponse createDriver(CreateDriverRequest request) {
        Driver driver = new Driver(request.name().trim(), request.phoneNumber());
        return driverMapper.toResponse(driverRepository.save(driver));
    }

    @Override
    public DriverResponse updateDriver(Long driverId, UpdateDriverRequest request) {
        Driver driver = getDriverEntity(driverId);
        driver.updateDetails(request.name().trim(), request.phoneNumber());
        return driverMapper.toResponse(driverRepository.save(driver));
    }

    @Override
    @Transactional(readOnly = true)
    public DriverResponse getDriver(Long driverId) {
        return driverMapper.toResponse(getDriverEntity(driverId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DriverResponse> searchDrivers(String name, Boolean active, int page, int size) {
        String normalizedName = name == null || name.isBlank() ? null : name.trim();
        return driverRepository.search(normalizedName, active, PageRequestUtil.create(page, size)).map(driverMapper::toResponse);
    }

    @Override
    public DriverResponse activateDriver(Long driverId) {
        Driver driver = getDriverEntity(driverId);
        driver.activate();
        return driverMapper.toResponse(driverRepository.save(driver));
    }

    @Override
    public DriverResponse deactivateDriver(Long driverId) {
        Driver driver = getDriverEntity(driverId);
        driver.deactivate();
        return driverMapper.toResponse(driverRepository.save(driver));
    }

    private Driver getDriverEntity(Long driverId) {
        return driverRepository.findById(driverId).orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + driverId));
    }
}
