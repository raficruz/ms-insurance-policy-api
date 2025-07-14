package br.com.acme.insurance.adapter.controller.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InsuranceByCustomerNotFoundException extends RuntimeException {

    private final UUID customerId;
    public InsuranceByCustomerNotFoundException(UUID customerId) {
        this.customerId = customerId;
    }
}
