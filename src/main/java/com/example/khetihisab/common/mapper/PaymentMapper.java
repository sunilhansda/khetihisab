package com.example.khetihisab.common.mapper;

import com.example.khetihisab.dto.payment.PaymentAllocationResponse;
import com.example.khetihisab.dto.payment.PaymentDetailsResponse;
import com.example.khetihisab.dto.payment.PaymentSummaryResponse;
import com.example.khetihisab.repository.projection.PaymentAllocationProjection;
import com.example.khetihisab.repository.projection.PaymentSummaryProjection;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PaymentMapper {
    public PaymentSummaryResponse toSummaryResponse(PaymentSummaryProjection payment) {
        return new PaymentSummaryResponse(payment.getPaymentId(), payment.getCustomerId(), payment.getCustomerName(), payment.getPaymentDate(), payment.getAmount(), payment.getAllocatedAmount(), payment.getUnallocatedAmount(), payment.getPaymentMethod(), payment.getReferenceNumber(), payment.getNotes(), payment.getCreatedAt());
    }

    public PaymentDetailsResponse toDetailsResponse(PaymentSummaryProjection payment, List<PaymentAllocationProjection> allocations) {
        List<PaymentAllocationResponse> allocationResponses = allocations.stream()
                .map(a -> new PaymentAllocationResponse(a.getAllocationId(), a.getJobId(), a.getAmount()))
                .toList();
        return new PaymentDetailsResponse(payment.getPaymentId(), payment.getCustomerId(), payment.getCustomerName(), payment.getPaymentDate(), payment.getAmount(), payment.getAllocatedAmount(), payment.getUnallocatedAmount(), payment.getPaymentMethod(), payment.getReferenceNumber(), payment.getNotes(), allocationResponses, payment.getCreatedAt());
    }
}
