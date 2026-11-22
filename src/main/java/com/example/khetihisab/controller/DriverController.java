package com.example.khetihisab.controller;

import com.example.khetihisab.dto.driver.CreateDriverRequest;
import com.example.khetihisab.dto.driver.DriverResponse;
import com.example.khetihisab.dto.driver.UpdateDriverRequest;
import com.example.khetihisab.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/drivers")
@Tag(name = "Drivers", description = "Driver management APIs")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    @Operation(summary = "Create a driver")
    public ResponseEntity<DriverResponse> createDriver(
            @Valid @RequestBody CreateDriverRequest request) {
        DriverResponse response = driverService.createDriver(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{driverId}")
    @Operation(summary = "Update driver details")
    public ResponseEntity<DriverResponse> updateDriver(
            @PathVariable Long driverId,
            @Valid @RequestBody UpdateDriverRequest request) {
        return ResponseEntity.ok(driverService.updateDriver(driverId, request));
    }

    @GetMapping("/{driverId}")
    @Operation(summary = "Get driver by ID")
    public ResponseEntity<DriverResponse> getDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverService.getDriver(driverId));
    }

    @GetMapping
    @Operation(summary = "Search drivers with pagination")
    public ResponseEntity<Page<DriverResponse>> searchDrivers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(driverService.searchDrivers(name, active, page, size));
    }

    @PatchMapping("/{driverId}/activate")
    @Operation(summary = "Activate a driver")
    public ResponseEntity<DriverResponse> activateDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverService.activateDriver(driverId));
    }

    @PatchMapping("/{driverId}/deactivate")
    @Operation(summary = "Deactivate a driver")
    public ResponseEntity<DriverResponse> deactivateDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverService.deactivateDriver(driverId));
    }
}

