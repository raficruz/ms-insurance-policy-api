package br.com.acme.insurance.infrastructure.messaging.consumer;

import br.com.acme.insurance.application.usecase.ProcessEventsUseCase;
import br.com.acme.insurance.infrastructure.messaging.dto.SubscriptionAuthorizationEvent;
import br.com.acme.insurance.shared.enums.SubscriptionStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionAuthorizationConsumer {

    private final ProcessEventsUseCase useCase;

    public SubscriptionAuthorizationConsumer(ProcessEventsUseCase useCase) {
        this.useCase = useCase;
    }

    @KafkaListener(topics = "subscription-authorization", groupId = "insurance-policy")
    public void consume(SubscriptionAuthorizationEvent event) {
        useCase.processSubscriptionAuthorization(
                event.insuranceId(),
                SubscriptionStatus.valueOf(event.status())
        );
    }
}
