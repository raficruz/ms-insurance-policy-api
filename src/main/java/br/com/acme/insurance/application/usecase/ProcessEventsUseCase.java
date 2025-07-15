package br.com.acme.insurance.application.usecase;

import br.com.acme.insurance.domain.model.CustomerAnalysis;
import br.com.acme.insurance.domain.policy.EligibilityPolicy;
import br.com.acme.insurance.domain.repository.InsuranceRepository;
import br.com.acme.insurance.shared.enums.InsuranceStatus;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProcessEventsUseCase {

    private final InsuranceRepository repository;
    private final List<EligibilityPolicy> policies;

    public ProcessEventsUseCase(InsuranceRepository repository, List<EligibilityPolicy> policies) {
        this.repository = repository;
        this.policies = policies;
    }

    public void processCustomerAnalysis(CustomerAnalysis analysis) {
        repository.findById(analysis.getInsuranceId()).ifPresent(insurance -> {
            insurance.setCustomerType(analysis.getCustomerType());

            boolean approved = policies.stream()
                    .anyMatch(policy -> policy.isEligible(insurance));

            InsuranceStatus newStatus = approved ? InsuranceStatus.VALIDATED : InsuranceStatus.REJECTED;
            insurance.setStatus(newStatus);
            insurance.addStatusHistory(newStatus, OffsetDateTime.now());

            repository.save(insurance);
        });
    }

    public void processEligibility(UUID insuranceId) {
        repository.findById(insuranceId).ifPresent(insurance -> {
            boolean approved = policies.stream()
                    .anyMatch(policy -> policy.isEligible(insurance));

            InsuranceStatus newStatus = approved ? InsuranceStatus.APPROVED : InsuranceStatus.REJECTED;
            insurance.setStatus(newStatus);
            insurance.addStatusHistory(newStatus, OffsetDateTime.now());

            repository.save(insurance);
        });
    }

    public void processPaymentConfirmation(UUID insuranceId, InsuranceStatus status) {
        repository.findById(insuranceId).ifPresent(insurance -> {
            insurance.setStatus(InsuranceStatus.VALIDATED);
            insurance.addStatusHistory(InsuranceStatus.VALIDATED, OffsetDateTime.now());

            repository.save(insurance);
        });
    }

    public void processSubscriptionAuthorization(UUID insuranceId, InsuranceStatus status) {
        repository.findById(insuranceId).ifPresent(insurance -> {
            InsuranceStatus newStatus = (status == InsuranceStatus.VALIDATED)
                    ? InsuranceStatus.APPROVED
                    : InsuranceStatus.REJECTED;
            insurance.setStatus(newStatus);
            insurance.addStatusHistory(newStatus, OffsetDateTime.now());

            repository.save(insurance);
        });
    }

    public void updateStatusToReceived(UUID insuranceId) {
        repository.findById(insuranceId).ifPresent(insurance -> {
            insurance.setStatus(InsuranceStatus.RECEIVED);
            insurance.addStatusHistory(InsuranceStatus.RECEIVED, OffsetDateTime.now());
            repository.save(insurance);
        });
    }
}