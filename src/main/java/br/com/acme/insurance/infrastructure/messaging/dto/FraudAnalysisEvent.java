package br.com.acme.insurance.infrastructure.messaging.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record FraudAnalysisEvent(
        UUID insuranceId,
        UUID customerId,
        String customerType,
        OffsetDateTime analyzedAt,

        List<String> findings
) {}
