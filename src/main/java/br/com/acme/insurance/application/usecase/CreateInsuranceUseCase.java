package br.com.acme.insurance.application.usecase;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.repository.InsuranceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateInsuranceUseCase {

    private final InsuranceRepository repository;

    public CreateInsuranceUseCase(InsuranceRepository repository) {
        this.repository = repository;
    }

    public UUID execute(Insurance insurance) {
        var saved = repository.save(insurance);
        return saved.getId();
    }
}
