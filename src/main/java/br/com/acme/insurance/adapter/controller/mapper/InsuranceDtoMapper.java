package br.com.acme.insurance.adapter.controller.mapper;

import br.com.acme.insurance.adapter.controller.dto.CreateInsuranceRequestInput;
import br.com.acme.insurance.adapter.controller.dto.CreateInsuranceRequestOutput;
import br.com.acme.insurance.adapter.controller.dto.InsuranceDetails;
import br.com.acme.insurance.adapter.controller.dto.StatusChangeDTO;
import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.model.StatusChange;
import br.com.acme.insurance.shared.enums.InsuranceStatus;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class InsuranceDtoMapper {

    public Insurance toDomain(CreateInsuranceRequestInput input) {
        return Insurance.builder()
                .id(UUID.randomUUID())
                .customerId(input.getCustomerId())
                .productId(input.getProductId())
                .category(input.getCategory())
                .salesChannel(input.getSalesChannel())
                .paymentMethod(input.getPaymentMethod())
                .status(InsuranceStatus.RECEIVED)
                .createdAt(OffsetDateTime.now())
                .coverages(input.getCoverages())
                .assistances(input.getAssistances())
                .insuredAmount(input.getInsuredAmount())
                .totalMonthlyPremiumAmount(input.getTotalMonthlyPremiumAmount())
                .history(List.of())
                .build();
    }

    public CreateInsuranceRequestOutput toOutput(UUID id, OffsetDateTime createdAt) {
        return new CreateInsuranceRequestOutput(id, createdAt);
    }

    public InsuranceDetails toDetails(Insurance insurance) {
        return InsuranceDetails.builder()
                .id(insurance.getId())
                .customerId(insurance.getCustomerId())
                .productId(insurance.getProductId())
                .category(insurance.getCategory())
                .salesChannel(insurance.getSalesChannel())
                .paymentMethod(insurance.getPaymentMethod())
                .status(insurance.getStatus().name())
                .createdAt(insurance.getCreatedAt())
                .finishedAt(insurance.getFinishedAt())
                .totalMonthlyPremiumAmount(insurance.getTotalMonthlyPremiumAmount())
                .insuredAmount(insurance.getInsuredAmount())
                .coverages(insurance.getCoverages())
                .assistances(insurance.getAssistances())
                .history(toDtoHistory(insurance.getHistory()))
                .build();
    }

    private List<StatusChangeDTO> toDtoHistory(List<StatusChange> history) {
        return history.stream()
                .map(change -> new StatusChangeDTO(change.status(), change.timestamp()))
                .collect(Collectors.toList());
    }
}
