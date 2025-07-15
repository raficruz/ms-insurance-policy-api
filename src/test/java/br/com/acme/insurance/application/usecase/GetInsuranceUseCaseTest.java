package br.com.acme.insurance.application.usecase;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.repository.InsuranceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetInsuranceUseCaseTest {

    private InsuranceRepository repository;
    private GetInsuranceUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(InsuranceRepository.class);
        useCase = new GetInsuranceUseCase(repository);
    }

    @Test
    void deveRetornarInsurancePorIdQuandoExistir() {
        UUID id = UUID.randomUUID();
        Insurance insurance = mock(Insurance.class);
        when(repository.findById(id)).thenReturn(Optional.of(insurance));

        Optional<Insurance> result = useCase.byId(id);

        assertTrue(result.isPresent());
        assertEquals(insurance, result.get());
        verify(repository).findById(id);
    }

    @Test
    void deveRetornarOptionalVazioQuandoInsuranceNaoExistir() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Insurance> result = useCase.byId(id);

        assertFalse(result.isPresent());
        verify(repository).findById(id);
    }

    @Test
    void deveRetornarListaDeInsurancesPorCustomerId() {
        UUID customerId = UUID.randomUUID();
        Insurance insurance1 = mock(Insurance.class);
        Insurance insurance2 = mock(Insurance.class);
        List<Insurance> insurances = List.of(insurance1, insurance2);

        when(repository.findByCustomerId(customerId)).thenReturn(insurances);

        List<Insurance> result = useCase.byCustomerId(customerId);

        assertEquals(insurances, result);
        verify(repository).findByCustomerId(customerId);
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumInsuranceParaCustomerId() {
        UUID customerId = UUID.randomUUID();
        when(repository.findByCustomerId(customerId)).thenReturn(List.of());

        List<Insurance> result = useCase.byCustomerId(customerId);

        assertTrue(result.isEmpty());
        verify(repository).findByCustomerId(customerId);
    }
}