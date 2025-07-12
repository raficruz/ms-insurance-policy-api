package br.com.acme.insurance.infrastructure.database.mapper;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.model.StatusChange;
import br.com.acme.insurance.infrastructure.database.entity.InsuranceEntity;
import br.com.acme.insurance.infrastructure.database.entity.StatusChangeEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class InsuranceMapper {

    public Insurance toDomain(InsuranceEntity entity) {
        if (entity == null) {
            return null;
        }

        return Insurance.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .productId(entity.getProductId())
                .category(entity.getCategory())
                .salesChannel(entity.getSalesChannel())
                .paymentMethod(entity.getPaymentMethod())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .finishedAt(entity.getFinishedAt() != null ? entity.getFinishedAt() : null)
                .totalMonthlyPremiumAmount(entity.getTotalMonthlyPremiumAmount())
                .insuredAmount(entity.getInsuredAmount())
                .coverages(entity.getCoverages())
                .assistances(entity.getAssistances())
                .history(toDomainHistory(entity.getHistory()))
                .build();
    }

    public InsuranceEntity toEntity(Insurance domain) {
        if (domain == null) {
            return null;
        }

        return InsuranceEntity.builder()
                .id(domain.getId() != null ? domain.getId() : UUID.randomUUID())
                .customerId(domain.getCustomerId())
                .productId(domain.getProductId())
                .category(domain.getCategory())
                .salesChannel(domain.getSalesChannel())
                .paymentMethod(domain.getPaymentMethod())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .finishedAt(domain.getFinishedAt() != null ? domain.getFinishedAt() : null)
                .totalMonthlyPremiumAmount(domain.getTotalMonthlyPremiumAmount())
                .insuredAmount(domain.getInsuredAmount())
                .coverages(domain.getCoverages())
                .assistances(domain.getAssistances())
                .history(toEntityHistory(domain.getHistory()))
                .build();
    }

    private List<StatusChange> toDomainHistory(List<StatusChangeEntity> entities) {
        if (entities == null) return List.of();

        return entities.stream()
                .map(e -> new StatusChange(
                        e.getStatus(),
                        e.getTimestamp()
                ))
                .collect(Collectors.toList());
    }

    private List<StatusChangeEntity> toEntityHistory(List<StatusChange> domainList) {
        if (domainList == null) return List.of();

        return domainList.stream()
                .map(d -> new StatusChangeEntity(
                        d.status(),
                        d.timestamp()
                ))
                .collect(Collectors.toList());
    }
}
