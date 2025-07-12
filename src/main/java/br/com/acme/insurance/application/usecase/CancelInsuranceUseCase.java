package br.com.acme.insurance.application.usecase;

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
        repository.findById(id).ifPresent(insurance -> {
            insurance.setStatus(InsuranceStatus.CANCELED);
            insurance.addStatusHistory(InsuranceStatus.CANCELED, OffsetDateTime.now());

            repository.save(insurance);
        });
    }
}
