package br.com.acme.insurance.application.usecase;

import br.com.acme.insurance.domain.model.CustomerAnalysis;
import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.policy.EligibilityPolicy;
import br.com.acme.insurance.domain.repository.InsuranceRepository;
import br.com.acme.insurance.shared.enums.CustomerType;
import br.com.acme.insurance.shared.enums.InsuranceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class ProcessEventsUseCaseTest {

    private InsuranceRepository repository;
    private EligibilityPolicy policy;
    private ProcessEventsUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(InsuranceRepository.class);
        policy = mock(EligibilityPolicy.class);
        useCase = new ProcessEventsUseCase(repository, List.of(policy));
    }

    @Test
    void processCustomerAnalysis_aprovaEValida() {
        UUID id = UUID.randomUUID();
        Insurance insurance = mock(Insurance.class);
        CustomerAnalysis analysis = CustomerAnalysis.builder()
                .insuranceId(id)
                .customerType(CustomerType.PREFERRED)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(insurance));
        when(policy.isEligible(insurance)).thenReturn(true);

        useCase.processCustomerAnalysis(analysis);

        verify(insurance).setCustomerType(CustomerType.PREFERRED);
        verify(insurance).setStatus(InsuranceStatus.VALIDATED);
        verify(insurance).addStatusHistory(eq(InsuranceStatus.VALIDATED), any(OffsetDateTime.class));
        verify(repository).save(insurance);
    }

    @Test
    void processCustomerAnalysis_rejeita() {
        UUID id = UUID.randomUUID();
        Insurance insurance = mock(Insurance.class);
        CustomerAnalysis analysis = CustomerAnalysis.builder()
                .insuranceId(id)
                .customerType(CustomerType.HIGH_RISK)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(insurance));
        when(policy.isEligible(insurance)).thenReturn(false);

        useCase.processCustomerAnalysis(analysis);

        verify(insurance).setStatus(InsuranceStatus.REJECTED);
        verify(insurance).addStatusHistory(eq(InsuranceStatus.REJECTED), any(OffsetDateTime.class));
        verify(repository).save(insurance);
    }

    @Test
    void processEligibility_aprova() {
        UUID id = UUID.randomUUID();
        Insurance insurance = mock(Insurance.class);

        when(repository.findById(id)).thenReturn(Optional.of(insurance));
        when(policy.isEligible(insurance)).thenReturn(true);

        useCase.processEligibility(id);

        verify(insurance).setStatus(InsuranceStatus.APPROVED);
        verify(insurance).addStatusHistory(eq(InsuranceStatus.APPROVED), any(OffsetDateTime.class));
        verify(repository).save(insurance);
    }

    @Test
    void processEligibility_rejeita() {
        UUID id = UUID.randomUUID();
        Insurance insurance = mock(Insurance.class);

        when(repository.findById(id)).thenReturn(Optional.of(insurance));
        when(policy.isEligible(insurance)).thenReturn(false);

        useCase.processEligibility(id);

        verify(insurance).setStatus(InsuranceStatus.REJECTED);
        verify(insurance).addStatusHistory(eq(InsuranceStatus.REJECTED), any(OffsetDateTime.class));
        verify(repository).save(insurance);
    }

    @Test
    void processPaymentConfirmation_valida() {
        UUID id = UUID.randomUUID();
        Insurance insurance = mock(Insurance.class);

        when(repository.findById(id)).thenReturn(Optional.of(insurance));

        useCase.processPaymentConfirmation(id, InsuranceStatus.APPROVED);

        verify(insurance).setStatus(InsuranceStatus.VALIDATED);
        verify(insurance).addStatusHistory(eq(InsuranceStatus.VALIDATED), any(OffsetDateTime.class));
        verify(repository).save(insurance);
    }

    @Test
    void processSubscriptionAuthorization_aprova() {
        UUID id = UUID.randomUUID();
        Insurance insurance = mock(Insurance.class);

        when(repository.findById(id)).thenReturn(Optional.of(insurance));

        useCase.processSubscriptionAuthorization(id, InsuranceStatus.VALIDATED);

        verify(insurance).setStatus(InsuranceStatus.APPROVED);
        verify(insurance).addStatusHistory(eq(InsuranceStatus.APPROVED), any(OffsetDateTime.class));
        verify(repository).save(insurance);
    }

    @Test
    void processSubscriptionAuthorization_rejeita() {
        UUID id = UUID.randomUUID();
        Insurance insurance = mock(Insurance.class);

        when(repository.findById(id)).thenReturn(Optional.of(insurance));

        useCase.processSubscriptionAuthorization(id, InsuranceStatus.REJECTED);

        verify(insurance).setStatus(InsuranceStatus.REJECTED);
        verify(insurance).addStatusHistory(eq(InsuranceStatus.REJECTED), any(OffsetDateTime.class));
        verify(repository).save(insurance);
    }

    @Test
    void updateStatusToReceived() {
        UUID id = UUID.randomUUID();
        Insurance insurance = mock(Insurance.class);

        when(repository.findById(id)).thenReturn(Optional.of(insurance));

        useCase.updateStatusToReceived(id);

        verify(insurance).setStatus(InsuranceStatus.RECEIVED);
        verify(insurance).addStatusHistory(eq(InsuranceStatus.RECEIVED), any(OffsetDateTime.class));
        verify(repository).save(insurance);
    }

    @Test
    void metodosNaoExecutamQuandoInsuranceNaoExiste() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        useCase.processCustomerAnalysis(CustomerAnalysis.builder().insuranceId(id).build());
        useCase.processEligibility(id);
        useCase.processPaymentConfirmation(id, InsuranceStatus.APPROVED);
        useCase.processSubscriptionAuthorization(id, InsuranceStatus.VALIDATED);
        useCase.updateStatusToReceived(id);

        verify(repository, times(5)).findById(id);
    }
}