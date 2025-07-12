package br.com.acme.insurance.infrastructure.messaging.consumer;

import br.com.acme.insurance.application.usecase.ProcessEventsUseCase;
import br.com.acme.insurance.domain.model.CustomerAnalysis;
import br.com.acme.insurance.infrastructure.messaging.dto.FraudAnalysisEvent;
import br.com.acme.insurance.shared.enums.CustomerType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FraudAnalysisConsumer {

    private final ProcessEventsUseCase useCase;

    public FraudAnalysisConsumer(ProcessEventsUseCase useCase) {
        this.useCase = useCase;
    }

    @KafkaListener(topics = "fraud-analysis", groupId = "insurance-policy")
    public void consume(FraudAnalysisEvent event) {
        var analysis = CustomerAnalysis.builder()
                .insuranceId(event.insuranceId())
                .customerId(event.customerId())
                .customerType(CustomerType.valueOf(event.customerType()))
                .assessedAt(event.analyzedAt())
                .findings(event.findings())
                .build();

        useCase.processCustomerAnalysis(analysis);
    }
}
