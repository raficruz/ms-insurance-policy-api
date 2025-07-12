package br.com.acme.insurance.infrastructure.database.entity;

import br.com.acme.insurance.shared.enums.InsuranceCategory;
import br.com.acme.insurance.shared.enums.InsuranceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "insurance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Access(AccessType.FIELD)
public class InsuranceEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsuranceCategory category;

    @Column(name = "sales_channel", nullable = false)
    private String salesChannel;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsuranceStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "finished_at")
    private OffsetDateTime finishedAt;

    @Column(name = "total_monthly_premium_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalMonthlyPremiumAmount;

    @Column(name = "insured_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal insuredAmount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "insurance_coverages", joinColumns = @JoinColumn(name = "insurance_id"))
    @MapKeyColumn(name = "coverage_name")
    @Column(name = "coverage_value")
    private Map<String, BigDecimal> coverages;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "insurance_assistances", joinColumns = @JoinColumn(name = "insurance_id"))
    @Column(name = "assistance")
    private List<String> assistances;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "insurance_status_history", joinColumns = @JoinColumn(name = "insurance_id"))
    private List<StatusChangeEntity> history;
}
