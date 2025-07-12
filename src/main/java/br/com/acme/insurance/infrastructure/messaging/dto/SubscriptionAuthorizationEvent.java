package br.com.acme.insurance.infrastructure.messaging.dto;

import java.util.UUID;

public record SubscriptionAuthorizationEvent(
        UUID insuranceId,
        String status
) {}
