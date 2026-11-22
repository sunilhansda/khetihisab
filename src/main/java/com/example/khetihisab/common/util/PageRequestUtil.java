package com.example.khetihisab.common.util;

import com.example.khetihisab.common.exception.BusinessRuleException;
import com.example.khetihisab.config.PaginationConfig;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public final class PageRequestUtil {
    private PageRequestUtil() {}

    public static Pageable create(int page, int size) {
        if (page < 0) throw new BusinessRuleException("Page index cannot be negative");
        if (size < 1) throw new BusinessRuleException("Page size must be greater than zero");
        return PageRequest.of(page, Math.min(size, PaginationConfig.MAX_SIZE));
    }
}
