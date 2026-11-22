package com.example.khetihisab.repository.projection;

import java.math.BigDecimal;

public interface PaymentAllocationProjection {
    Long getAllocationId();
    Long getJobId();
    BigDecimal getAmount();
}