package br.com.acme.insurance.infrastructure.database.repository;

import br.com.acme.insurance.infrastructure.database.entity.InsuranceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface InsuranceJpaRepository extends JpaRepository<InsuranceEntity, UUID> {
    Optional<InsuranceEntity> findById(UUID id);

    List<InsuranceEntity> findByCustomerId(UUID customerId);

}
