package br.com.acme.insurance.adapter.controller.dto;

import br.com.acme.insurance.shared.enums.InsuranceStatus;

import java.time.OffsetDateTime;

public record StatusChangeDTO(
        InsuranceStatus status,
        OffsetDateTime timestamp
) {}
