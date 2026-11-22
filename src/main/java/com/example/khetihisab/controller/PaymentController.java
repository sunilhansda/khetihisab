package com.example.khetihisab.controller;

import com.example.khetihisab.dto.payment.CreatePaymentRequest;
import com.example.khetihisab.dto.payment.PaymentDetailsResponse;
import com.example.khetihisab.dto.payment.PaymentSummaryResponse;
import com.example.khetihisab.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/payments")
@Profile("database")
@Tag(name = "Payments", description = "Payment management APIs")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @Operation(summary = "Record a customer payment and optional job allocations")
    public ResponseEntity<PaymentDetailsResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {
        PaymentDetailsResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment details with allocations")
    public ResponseEntity<PaymentDetailsResponse> getPayment(
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentDetails(paymentId));
    }

    @GetMapping
    @Operation(summary = "Search payments with filters and pagination")
    public ResponseEntity<Page<PaymentSummaryResponse>> searchPayments(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(paymentService.searchPayments(
                customerId, fromDate, toDate, page, size));
    }
}

