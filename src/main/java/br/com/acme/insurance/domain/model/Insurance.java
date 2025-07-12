package br.com.acme.insurance.domain.model;

import br.com.acme.insurance.shared.enums.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Insurance {

    private UUID id;
    private UUID customerId;
    private UUID productId;
    private InsuranceCategory category;
    private CustomerType customerType;
    private InsuranceStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime finishedAt;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal insuredAmount;
    private Map<String, BigDecimal> coverages;
    private List<String> assistances = new ArrayList<>(0);
    private List<StatusChange> history = new ArrayList<>(0);
    private String salesChannel;
    private String paymentMethod;

    public void addStatusHistory(InsuranceStatus newStatus, OffsetDateTime timestamp) {
        this.history.add(new StatusChange(newStatus, timestamp));
    }
}