package br.com.acme.insurance.infrastructure.messaging.consumer;

import br.com.acme.insurance.application.usecase.ProcessEventsUseCase;
import br.com.acme.insurance.infrastructure.messaging.dto.PaymentConfirmationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentConfirmationConsumer {

    private final ProcessEventsUseCase useCase;

    public PaymentConfirmationConsumer(ProcessEventsUseCase useCase) {
        this.useCase = useCase;
    }

    @KafkaListener(
            topics = "payment-confirmation",
            groupId = "insurance-policy",
            properties = {"spring.json.value.default.type=br.com.acme.insurance.infrastructure.messaging.dto.PaymentConfirmationEvent"}
    )
    public void consume(PaymentConfirmationEvent event) {
        useCase.processPaymentConfirmation(event.insuranceId(), event.status());
    }
}
