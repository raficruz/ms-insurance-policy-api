package br.com.acme.insurance.domain.model;

import br.com.acme.insurance.shared.enums.SubscriptionStatus;

import java.util.UUID;

public record SubscriptionEvent(
        UUID insuranceId,
        SubscriptionStatus status
) {}
