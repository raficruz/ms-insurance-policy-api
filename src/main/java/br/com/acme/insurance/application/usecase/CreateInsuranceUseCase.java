package br.com.acme.insurance.application.usecase;

import br.com.acme.insurance.application.port.FraudAnalysisClientPort;
import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.repository.InsuranceRepository;
import br.com.acme.insurance.infrastructure.messaging.publisher.InsuranceEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateInsuranceUseCase {

    private final InsuranceRepository repository;
    private final FraudAnalysisClientPort fraudAnalysisClient;
    private final InsuranceEventPublisher eventPublisher;

    public CreateInsuranceUseCase(InsuranceRepository repository, FraudAnalysisClientPort fraudAnalysisClient, InsuranceEventPublisher eventPublisher) {
        this.repository = repository;
        this.fraudAnalysisClient = fraudAnalysisClient;
        this.eventPublisher = eventPublisher;
    }

    public UUID execute(Insurance insurance) {
        Insurance saved = repository.save(insurance);
        eventPublisher.publishReceivedEvent(saved.getId());
        fraudAnalysisClient.analyze(saved);
        return saved.getId();
    }
}