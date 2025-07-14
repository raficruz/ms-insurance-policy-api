package br.com.acme.insurance.adapter.controller.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InsuranceNotFoundException extends RuntimeException {

    private final UUID id;

    public InsuranceNotFoundException(UUID id) {
        this.id = id;
    }

}
