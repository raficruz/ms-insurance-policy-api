package br.com.acme.insurance.application.usecase;

import br.com.acme.insurance.domain.model.CustomerAnalysis;
import br.com.acme.insurance.domain.policy.EligibilityPolicy;
import br.com.acme.insurance.domain.repository.InsuranceRepository;
import br.com.acme.insurance.shared.enums.InsuranceStatus;
import br.com.acme.insurance.shared.enums.SubscriptionStatus;
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

            insurance.setStatus(approved ? InsuranceStatus.APPROVED : InsuranceStatus.REJECTED);
            insurance.addStatusHistory(insurance.getStatus(), OffsetDateTime.now());

            repository.save(insurance);
        });
    }

    public void processEligibility(UUID insuranceId) {
        repository.findById(insuranceId).ifPresent(insurance -> {
            boolean approved = policies.stream()
                    .anyMatch(policy -> policy.isEligible(insurance));

            insurance.setStatus(approved ? InsuranceStatus.APPROVED : InsuranceStatus.REJECTED);
            insurance.addStatusHistory(insurance.getStatus(), OffsetDateTime.now());

            repository.save(insurance);
        });
    }

    public void processPaymentConfirmation(UUID insuranceId) {
        repository.findById(insuranceId).ifPresent(insurance -> {
            insurance.setStatus(InsuranceStatus.VALIDATED);
            insurance.addStatusHistory(InsuranceStatus.VALIDATED, OffsetDateTime.now());

            repository.save(insurance);
        });
    }

    public void processSubscriptionAuthorization(UUID insuranceId, SubscriptionStatus status) {
        repository.findById(insuranceId).ifPresent(insurance -> {
            if (status == SubscriptionStatus.AUTHORIZED) {
                insurance.setStatus(InsuranceStatus.APPROVED);
            } else {
                insurance.setStatus(InsuranceStatus.REJECTED);
            }
            insurance.addStatusHistory(insurance.getStatus(), OffsetDateTime.now());

            repository.save(insurance);
        });
    }
}
