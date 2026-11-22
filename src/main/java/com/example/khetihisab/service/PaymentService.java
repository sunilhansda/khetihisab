package com.example.khetihisab.service;

import com.example.khetihisab.dto.payment.CreatePaymentRequest;
import com.example.khetihisab.dto.payment.PaymentDetailsResponse;
import com.example.khetihisab.dto.payment.PaymentSummaryResponse;
import org.springframework.data.domain.Page;
import java.time.LocalDate;

public interface PaymentService {
    PaymentDetailsResponse createPayment(CreatePaymentRequest request);
    PaymentDetailsResponse getPaymentDetails(Long paymentId);
    Page<PaymentSummaryResponse> searchPayments(Long customerId, LocalDate fromDate, LocalDate toDate, int page, int size);
}
