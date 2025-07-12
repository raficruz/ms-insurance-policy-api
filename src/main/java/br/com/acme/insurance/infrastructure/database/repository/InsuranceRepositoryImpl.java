package br.com.acme.insurance.infrastructure.database.repository;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.repository.InsuranceRepository;
import br.com.acme.insurance.infrastructure.database.mapper.InsuranceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InsuranceRepositoryImpl implements InsuranceRepository {

    private final InsuranceJpaRepository jpaRepository;
    private final InsuranceMapper mapper;

    @Override
    public Optional<Insurance> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Insurance> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Insurance save(Insurance insurance) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(insurance)));
    }
}

