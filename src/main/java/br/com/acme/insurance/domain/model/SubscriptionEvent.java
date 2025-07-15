package br.com.acme.insurance.domain.model;

import br.com.acme.insurance.shared.enums.InsuranceStatus;

import java.util.UUID;

public record SubscriptionEvent(
        UUID insuranceId,
        InsuranceStatus status
) {}
