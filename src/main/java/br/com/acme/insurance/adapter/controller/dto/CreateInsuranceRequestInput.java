package br.com.acme.insurance.adapter.controller.dto;

import br.com.acme.insurance.shared.enums.InsuranceCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInsuranceRequestInput {

    @NotNull
    @JsonProperty("customer_id")
    private UUID customerId;

    @NotNull
    @JsonProperty("product_id")
    private UUID productId;

    @NotNull
    @JsonProperty("category")
    private InsuranceCategory category;

    @NotBlank
    @JsonProperty("salesChannel")
    private String salesChannel;

    @NotBlank
    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true, message = "{javax.validation.constraints.DecimalMin.message}")
    @JsonProperty("total_monthly_premium_amount")
    private BigDecimal totalMonthlyPremiumAmount;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true, message = "{javax.validation.constraints.DecimalMin.message}")
    @JsonProperty("insured_amount")
    private BigDecimal insuredAmount;

    @NotNull
    @Size(min = 1, message = "{javax.validation.constraints.Size.collection}")
    @JsonProperty("coverages")
    private Map<String, BigDecimal> coverages;

    @NotNull
    @Size(min = 1, message = "{javax.validation.constraints.Size.collection}")
    @JsonProperty("assistances")
    private List<String> assistances;
}