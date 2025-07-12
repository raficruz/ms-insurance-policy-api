package br.com.acme.insurance.domain.repository;

import br.com.acme.insurance.domain.model.Insurance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InsuranceRepository {
    Optional<Insurance> findById(UUID id);
    List<Insurance> findByCustomerId(UUID customerId);
    Insurance save(Insurance insurance);
}
