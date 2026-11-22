package com.example.khetihisab.service.impl;

import com.example.khetihisab.common.exception.ResourceNotFoundException;
import com.example.khetihisab.common.mapper.CustomerMapper;
import com.example.khetihisab.common.util.PageRequestUtil;
import com.example.khetihisab.dto.customer.CreateCustomerRequest;
import com.example.khetihisab.dto.customer.CustomerResponse;
import com.example.khetihisab.dto.customer.UpdateCustomerRequest;
import com.example.khetihisab.entity.Customer;
import com.example.khetihisab.repository.CustomerRepository;
import com.example.khetihisab.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer(request.name().trim(), request.phoneNumber(), normalize(request.addressCode()), request.address());
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request) {
        Customer customer = getCustomerEntity(customerId);
        customer.updateDetails(request.name().trim(), request.phoneNumber(), normalize(request.addressCode()), request.address());
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(Long customerId) {
        return customerMapper.toResponse(getCustomerEntity(customerId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> searchCustomers(String name, String addressCode, int page, int size) {
        String normalizedName = normalize(name);
        String normalizedAddressCode = normalize(addressCode);
        return customerRepository.search(normalizedName, normalizedAddressCode, PageRequestUtil.create(page, size)).map(customerMapper::toResponse);
    }

    private Customer getCustomerEntity(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
