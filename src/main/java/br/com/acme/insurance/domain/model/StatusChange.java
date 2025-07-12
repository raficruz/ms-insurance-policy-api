package br.com.acme.insurance.domain.model;

import br.com.acme.insurance.shared.enums.InsuranceStatus;

import java.time.Instant;
import java.time.OffsetDateTime;

public record StatusChange(
        InsuranceStatus status,
        OffsetDateTime timestamp
) {}
