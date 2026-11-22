package com.example.khetihisab.service.impl;

import com.example.khetihisab.common.exception.BusinessRuleException;
import com.example.khetihisab.common.exception.ResourceNotFoundException;
import com.example.khetihisab.common.mapper.PaymentMapper;
import com.example.khetihisab.common.util.PageRequestUtil;
import com.example.khetihisab.dto.payment.CreatePaymentRequest;
import com.example.khetihisab.dto.payment.PaymentAllocationRequest;
import com.example.khetihisab.dto.payment.PaymentDetailsResponse;
import com.example.khetihisab.dto.payment.PaymentSummaryResponse;
import com.example.khetihisab.entity.CultivationJob;
import com.example.khetihisab.entity.Customer;
import com.example.khetihisab.entity.Payment;
import com.example.khetihisab.entity.PaymentAllocation;
import com.example.khetihisab.enums.JobStatus;
import com.example.khetihisab.repository.CultivationJobRepository;
import com.example.khetihisab.repository.CustomerRepository;
import com.example.khetihisab.repository.PaymentAllocationRepository;
import com.example.khetihisab.repository.PaymentRepository;
import com.example.khetihisab.repository.projection.PaymentAllocationProjection;
import com.example.khetihisab.repository.projection.PaymentSummaryProjection;
import com.example.khetihisab.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentAllocationRepository allocationRepository;
    private final CultivationJobRepository jobRepository;
    private final CustomerRepository customerRepository;
    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentAllocationRepository allocationRepository, CultivationJobRepository jobRepository, CustomerRepository customerRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.allocationRepository = allocationRepository;
        this.jobRepository = jobRepository;
        this.customerRepository = customerRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public PaymentDetailsResponse createPayment(CreatePaymentRequest request) {
        Customer customer = customerRepository.findById(request.customerId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.customerId()));
        List<PaymentAllocationRequest> requestedAllocations = request.allocations() == null ? List.of() : request.allocations();
        validateRequestedAllocations(request.amount(), requestedAllocations);
        Payment payment = new Payment(customer, request.paymentDate(), request.amount(), request.paymentMethod(), request.referenceNumber(), request.notes());
        paymentRepository.saveAndFlush(payment);

        List<Long> jobIds = requestedAllocations.stream().map(PaymentAllocationRequest::jobId).sorted().toList();

        List<CultivationJob> lockedJobs = new ArrayList<>();
        for (Long jobId : jobIds) {
            CultivationJob job = jobRepository.findByIdForUpdate(jobId)
                    .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
            lockedJobs.add(job);
        }

        List<PaymentAllocation> allocations = new ArrayList<>();
        BigDecimal totalAllocated = BigDecimal.ZERO;
        for (int i = 0; i < lockedJobs.size(); i++) {
            CultivationJob job = lockedJobs.get(i);
            var allocationRequest = requestedAllocations.stream()
                    .filter(a -> a.jobId().equals(job.getJobId())).findFirst().orElseThrow();

            if (!job.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
                throw new BusinessRuleException("Payment and job must belong to the same customer");
            }
            if (job.getStatus() == JobStatus.CANCELLED) {
                throw new BusinessRuleException("Cannot allocate payment to a cancelled job");
            }

            BigDecimal alreadyAllocated = allocationRepository.getAllocatedAmountByJobId(job.getJobId());
            BigDecimal remainingBalance = job.getAmount().subtract(alreadyAllocated);
            if (allocationRequest.amount().compareTo(remainingBalance) > 0) {
                throw new BusinessRuleException("Allocation exceeds remaining balance for job id: " + job.getJobId());
            }

            totalAllocated = totalAllocated.add(allocationRequest.amount());
            allocations.add(new PaymentAllocation(payment, job, allocationRequest.amount()));
        }

        if (totalAllocated.compareTo(payment.getAmount()) > 0) {
            throw new BusinessRuleException("Total allocations cannot exceed payment amount");
        }

        if (!allocations.isEmpty()) allocationRepository.saveAllAndFlush(allocations);
        return getPaymentDetails(payment.getPaymentId());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDetailsResponse getPaymentDetails(Long paymentId) {
        PaymentSummaryProjection payment = paymentRepository.findSummaryByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
        List<PaymentAllocationProjection> allocations = allocationRepository.findDetailsByPaymentId(paymentId);
        return paymentMapper.toDetailsResponse(payment, allocations);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentSummaryResponse> searchPayments(Long customerId, LocalDate fromDate, LocalDate toDate, int page, int size) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) throw new BusinessRuleException("From date cannot be after to date");
        return paymentRepository.search(customerId, fromDate, toDate, PageRequestUtil.create(page, size)).map(paymentMapper::toSummaryResponse);
    }

    private void validateRequestedAllocations(BigDecimal paymentAmount, List<PaymentAllocationRequest> allocations) {
        if (allocations == null || allocations.isEmpty()) return;
        Set<Long> jobIds = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;
        for (var allocation : allocations) {
            if (!jobIds.add(allocation.jobId())) throw new BusinessRuleException("Duplicate job allocation in the same payment request: " + allocation.jobId());
            total = total.add(allocation.amount());
        }
        if (total.compareTo(paymentAmount) > 0) throw new BusinessRuleException("Total allocations cannot exceed payment amount");
    }
}
