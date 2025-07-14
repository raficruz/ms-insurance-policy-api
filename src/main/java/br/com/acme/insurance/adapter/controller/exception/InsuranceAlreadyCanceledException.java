package br.com.acme.insurance.adapter.controller.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InsuranceAlreadyCanceledException extends RuntimeException {
    private final UUID id;
    public InsuranceAlreadyCanceledException(UUID id) {
        this.id = id;
    }
}
