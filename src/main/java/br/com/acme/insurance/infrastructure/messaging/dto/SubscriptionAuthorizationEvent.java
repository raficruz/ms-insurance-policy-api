package br.com.acme.insurance.infrastructure.messaging.dto;

import br.com.acme.insurance.shared.enums.InsuranceStatus;

import java.util.UUID;

public record SubscriptionAuthorizationEvent(
        UUID insuranceId,
        InsuranceStatus status
) {}
