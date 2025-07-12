package br.com.acme.insurance.infrastructure.messaging.dto;

import java.util.UUID;

public record PaymentConfirmationEvent(
        UUID insuranceId
) {}
