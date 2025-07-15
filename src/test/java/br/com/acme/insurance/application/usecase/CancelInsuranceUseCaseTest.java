package br.com.acme.insurance.application.usecase;

import br.com.acme.insurance.adapter.controller.exception.InsuranceAlreadyCanceledException;
import br.com.acme.insurance.adapter.controller.exception.InsuranceNotFoundException;
import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.model.StatusChange;
import br.com.acme.insurance.domain.repository.InsuranceRepository;
import br.com.acme.insurance.shared.enums.InsuranceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CancelInsuranceUseCaseTest {

    private InsuranceRepository repository;
    private CancelInsuranceUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(InsuranceRepository.class);
        useCase = new CancelInsuranceUseCase(repository);
    }

    @Test
    void deveCancelarSeguroENaoLancarExcecao() {
        UUID id = UUID.randomUUID();
        Insurance insurance = mock(Insurance.class);

        when(repository.findById(id)).thenReturn(Optional.of(insurance));
        when(insurance.getStatus()).thenReturn(InsuranceStatus.RECEIVED);
        when(insurance.getHistory()).thenReturn(Collections.emptyList());

        doNothing().when(insurance).setStatus(InsuranceStatus.CANCELED);
        doNothing().when(insurance).addStatusHistory(eq(InsuranceStatus.CANCELED), any(OffsetDateTime.class));
        when(repository.save(insurance)).thenReturn(insurance);

        assertDoesNotThrow(() -> useCase.execute(id));
        verify(insurance).setStatus(InsuranceStatus.CANCELED);
        verify(insurance).addStatusHistory(eq(InsuranceStatus.CANCELED), any(OffsetDateTime.class));
        verify(repository).save(insurance);
    }

    @Test
    void deveLancarInsuranceNotFoundExceptionQuandoNaoEncontrarSeguro() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InsuranceNotFoundException.class, () -> useCase.execute(id));
        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarInsuranceAlreadyCanceledExceptionQuandoJaCanceladoEComHistorico() {
        UUID id = UUID.randomUUID();
        Insurance insurance = mock(Insurance.class);
        StatusChange history = new StatusChange(InsuranceStatus.CANCELED, OffsetDateTime.now());

        when(repository.findById(id)).thenReturn(Optional.of(insurance));
        when(insurance.getStatus()).thenReturn(InsuranceStatus.CANCELED);
        when(insurance.getHistory()).thenReturn(List.of(history));

        assertThrows(InsuranceAlreadyCanceledException.class, () -> useCase.execute(id));
        verify(repository, never()).save(any());
    }

    @Test
    void deveAdicionarHistoricoQuandoJaCanceladoSemHistorico() {
        UUID id = UUID.randomUUID();
        Insurance insurance = mock(Insurance.class);

        when(repository.findById(id)).thenReturn(Optional.of(insurance));
        when(insurance.getStatus()).thenReturn(InsuranceStatus.CANCELED);
        when(insurance.getHistory()).thenReturn(Collections.emptyList());

        doNothing().when(insurance).addStatusHistory(eq(InsuranceStatus.CANCELED), any(OffsetDateTime.class));
        when(repository.save(insurance)).thenReturn(insurance);

        useCase.execute(id);

        verify(insurance).addStatusHistory(eq(InsuranceStatus.CANCELED), any(OffsetDateTime.class));
        verify(repository).save(insurance);
    }
}