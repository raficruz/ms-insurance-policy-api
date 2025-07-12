package br.com.acme.insurance.application.usecase;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.repository.InsuranceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetInsuranceUseCase {

    private final InsuranceRepository repository;

    public GetInsuranceUseCase(InsuranceRepository repository) {
        this.repository = repository;
    }

    public Optional<Insurance> byId(UUID id) {
        return repository.findById(id);
    }

    public List<Insurance> byCustomerId(UUID customerId) {
        return repository.findByCustomerId(customerId);
    }
}
