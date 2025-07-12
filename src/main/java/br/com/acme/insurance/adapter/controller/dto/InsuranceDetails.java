package br.com.acme.insurance.adapter.controller.dto;

import br.com.acme.insurance.shared.enums.InsuranceCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceDetails {

    private UUID id;
    private UUID customerId;
    private UUID productId;
    private InsuranceCategory category;
    private String salesChannel;
    private String paymentMethod;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime finishedAt;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal insuredAmount;
    private Map<String, BigDecimal> coverages;
    private List<String> assistances;
    private List<StatusChangeDTO> history;
}
