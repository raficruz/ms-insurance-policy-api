package br.com.acme.insurance.infrastructure.messaging.consumer;

import br.com.acme.insurance.shared.enums.InsuranceCategory;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record InsuranceCreatedEvent(
        UUID insuranceId,
        UUID customerId,
        UUID productId,
        InsuranceCategory category,
        BigDecimal insuredAmount,
        BigDecimal totalMonthlyPremiumAmount,
        OffsetDateTime createdAt
) {}
