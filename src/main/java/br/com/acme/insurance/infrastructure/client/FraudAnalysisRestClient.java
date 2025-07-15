package br.com.acme.insurance.infrastructure.client;

import br.com.acme.insurance.application.port.FraudAnalysisClientPort;
import br.com.acme.insurance.application.usecase.ProcessEventsUseCase;
import br.com.acme.insurance.domain.model.CustomerAnalysis;
import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.infrastructure.client.dto.FraudAnalysisRequest;
import br.com.acme.insurance.infrastructure.messaging.dto.FraudAnalysisResponse;
import br.com.acme.insurance.shared.enums.CustomerType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component
public class FraudAnalysisRestClient implements FraudAnalysisClientPort {

    private final RestTemplate restTemplate;
    private final String fraudAnalysisUrl;

    private final ProcessEventsUseCase processEventsUseCase;

    public FraudAnalysisRestClient(
            RestTemplate restTemplate,
            @Value("${external.fraud-analysis.url}") String fraudAnalysisUrl,
            ProcessEventsUseCase processEventsUseCase
    ) {
        this.restTemplate = restTemplate;
        this.fraudAnalysisUrl = fraudAnalysisUrl;
        this.processEventsUseCase = processEventsUseCase;
    }

    @Override
    @Async
    public CompletableFuture<FraudAnalysisResponse> analyze(Insurance insurance) {
        FraudAnalysisRequest request = new FraudAnalysisRequest(insurance.getId(), insurance.getCustomerId());
        FraudAnalysisResponse response = restTemplate.postForObject(fraudAnalysisUrl, request, FraudAnalysisResponse.class);

        CustomerAnalysis analysis = CustomerAnalysis.builder()
                .insuranceId(response.insuranceId())
                .customerId(response.customerId())
                .customerType(CustomerType.valueOf(response.customerType()))
                .assessedAt(response.analyzedAt())
                .findings(response.findings())
                .build();

        processEventsUseCase.processCustomerAnalysis(analysis);

        return CompletableFuture.completedFuture(response);
    }
}