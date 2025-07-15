package br.com.acme.insurance.infrastructure.client.dto;

import java.util.UUID;

public record FraudAnalysisRequest(
        UUID insuranceId,
        UUID customerId
) {}