package br.com.acme.insurance.application.usecase;

import br.com.acme.insurance.adapter.controller.exception.InsuranceAlreadyCanceledException;
import br.com.acme.insurance.adapter.controller.exception.InsuranceNotFoundException;
import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.repository.InsuranceRepository;
import br.com.acme.insurance.shared.enums.InsuranceStatus;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class CancelInsuranceUseCase {

    private final InsuranceRepository repository;

    public CancelInsuranceUseCase(InsuranceRepository repository) {
        this.repository = repository;
    }

    public void execute(UUID id) {
        Insurance insurance = repository.findById(id)
                .orElseThrow(() -> new InsuranceNotFoundException(id));

        boolean isCanceled = InsuranceStatus.CANCELED.equals(insurance.getStatus());
        boolean hasCanceledHistory = insurance.getHistory() != null &&
                insurance.getHistory().stream().anyMatch(h -> InsuranceStatus.CANCELED.equals(h.status()));

        if (isCanceled) {
            if (hasCanceledHistory) {
                throw new InsuranceAlreadyCanceledException(id);
            } else {
                // Status já está cancelado, mas não há histórico: adiciona o histórico
                insurance.addStatusHistory(InsuranceStatus.CANCELED, OffsetDateTime.now());
                repository.save(insurance);
                return;
            }
        }

        insurance.setStatus(InsuranceStatus.CANCELED);
        insurance.addStatusHistory(InsuranceStatus.CANCELED, OffsetDateTime.now());
        repository.save(insurance);
    }
}
