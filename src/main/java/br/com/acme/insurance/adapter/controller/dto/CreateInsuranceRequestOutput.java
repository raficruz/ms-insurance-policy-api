package br.com.acme.insurance.adapter.controller.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateInsuranceRequestOutput(
        UUID id,
        OffsetDateTime createdAt) {}

