package br.com.acme.insurance.infrastructure.messaging.consumer;

import br.com.acme.insurance.application.usecase.ProcessEventsUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InsuranceReceivedConsumer {

    private final ProcessEventsUseCase processEventsUseCase;

    public InsuranceReceivedConsumer(ProcessEventsUseCase processEventsUseCase) {
        this.processEventsUseCase = processEventsUseCase;
    }

    @KafkaListener(
            topics = "insurance-received",
            groupId = "insurance-policy"
    )
    public void listen(ConsumerRecord<String, String> record) {
        UUID insuranceId = UUID.fromString(record.key());
        processEventsUseCase.updateStatusToReceived(insuranceId);
    }
}