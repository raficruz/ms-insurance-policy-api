package br.com.acme.insurance.adapter.controller.dto;

import br.com.acme.insurance.shared.enums.InsuranceCategory;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateInsuranceRequestInput(
        @JsonProperty("customer_id")
        UUID customerId,

        @JsonProperty("product_id")
        UUID productId,

        @JsonProperty("category")
        InsuranceCategory category,

        @JsonProperty("salesChannel")
        String salesChannel,

        @JsonProperty("paymentMethod")
        String paymentMethod,

        @JsonProperty("total_monthly_premium_amount")
        BigDecimal totalMonthlyPremiumAmount,

        @JsonProperty("insured_amount")
        BigDecimal insuredAmount,

        @JsonProperty("coverages")
        Map<String, BigDecimal> coverages,

        @JsonProperty("assistances")
        List<String> assistances
) {}
