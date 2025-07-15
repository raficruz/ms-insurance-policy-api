package br.com.acme.insurance.application.port;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.infrastructure.messaging.dto.FraudAnalysisResponse;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface FraudAnalysisClientPort {
    @Async
    CompletableFuture<FraudAnalysisResponse> analyze(Insurance insurance);
}