package br.com.acme.insurance.application.usecase;

import br.com.acme.insurance.application.port.FraudAnalysisClientPort;
import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.repository.InsuranceRepository;
import br.com.acme.insurance.infrastructure.messaging.publisher.InsuranceEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateInsuranceUseCaseTest {

    private InsuranceRepository repository;
    private FraudAnalysisClientPort fraudAnalysisClient;
    private InsuranceEventPublisher eventPublisher;
    private CreateInsuranceUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(InsuranceRepository.class);
        fraudAnalysisClient = mock(FraudAnalysisClientPort.class);
        eventPublisher = mock(InsuranceEventPublisher.class);
        useCase = new CreateInsuranceUseCase(repository, fraudAnalysisClient, eventPublisher);
    }

    @Test
    void deveSalvarSeguroPublicarEventoEChamarAnaliseFraudeERetornarId() {
        Insurance insurance = mock(Insurance.class);
        UUID id = UUID.randomUUID();

        when(repository.save(insurance)).thenReturn(insurance);
        when(insurance.getId()).thenReturn(id);

        UUID result = useCase.execute(insurance);

        assertEquals(id, result);
        verify(repository).save(insurance);
        verify(eventPublisher).publishReceivedEvent(id);
        verify(fraudAnalysisClient).analyze(insurance);
    }

    @Test
    void devePropagarExcecaoQuandoRepositoryFalha() {
        Insurance insurance = mock(Insurance.class);
        when(repository.save(insurance)).thenThrow(new RuntimeException("erro"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.execute(insurance));
        assertEquals("erro", ex.getMessage());
        verify(repository).save(insurance);
        verifyNoMoreInteractions(eventPublisher, fraudAnalysisClient);
    }

    @Test
    void devePropagarExcecaoQuandoEventPublisherFalha() {
        Insurance insurance = mock(Insurance.class);
        UUID id = UUID.randomUUID();

        when(repository.save(insurance)).thenReturn(insurance);
        when(insurance.getId()).thenReturn(id);
        doThrow(new RuntimeException("erro evento")).when(eventPublisher).publishReceivedEvent(id);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.execute(insurance));
        assertEquals("erro evento", ex.getMessage());
        verify(repository).save(insurance);
        verify(eventPublisher).publishReceivedEvent(id);
        verifyNoMoreInteractions(fraudAnalysisClient);
    }

    @Test
    void devePropagarExcecaoQuandoFraudAnalysisFalha() {
        Insurance insurance = mock(Insurance.class);
        UUID id = UUID.randomUUID();

        when(repository.save(insurance)).thenReturn(insurance);
        when(insurance.getId()).thenReturn(id);
        doThrow(new RuntimeException("erro fraude")).when(fraudAnalysisClient).analyze(insurance);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.execute(insurance));
        assertEquals("erro fraude", ex.getMessage());
        verify(repository).save(insurance);
        verify(eventPublisher).publishReceivedEvent(id);
        verify(fraudAnalysisClient).analyze(insurance);
    }

    @Test
    void deveRetornarNullQuandoGetIdRetornaNull() {
        Insurance insurance = mock(Insurance.class);

        when(repository.save(insurance)).thenReturn(insurance);
        when(insurance.getId()).thenReturn(null);

        UUID result = useCase.execute(insurance);

        assertNull(result);
        verify(repository).save(insurance);
        verify(eventPublisher).publishReceivedEvent(null);
        verify(fraudAnalysisClient).analyze(insurance);
    }
}