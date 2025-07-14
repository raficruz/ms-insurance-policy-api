package br.com.acme.insurance.application.usecase;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.repository.InsuranceRepository;
import br.com.acme.insurance.infrastructure.messaging.consumer.InsuranceCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateInsuranceUseCase {

    private final InsuranceRepository repository;
    private final KafkaTemplate<String, InsuranceCreatedEvent> kafkaTemplate;

    public CreateInsuranceUseCase(InsuranceRepository repository, KafkaTemplate<String, InsuranceCreatedEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public UUID execute(Insurance insurance) {
        var saved = repository.save(insurance);
        var event = new InsuranceCreatedEvent(
                saved.getId(),
                saved.getCustomerId(),
                saved.getProductId(),
                saved.getCategory(),
                saved.getInsuredAmount(),
                saved.getTotalMonthlyPremiumAmount(),
                saved.getCreatedAt()
        );
        kafkaTemplate.send("insurance-created-topic", event);
        return saved.getId();
    }
}
