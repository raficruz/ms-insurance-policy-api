package br.com.acme.insurance.infrastructure.messaging.consumer;

import br.com.acme.insurance.application.usecase.ProcessEventsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InsuranceCreatedConsumer {

    private final ProcessEventsUseCase processEventsUseCase;

    @KafkaListener(
            topics = "${kafka.topics.insurance-created}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "insuranceCreatedKafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, InsuranceCreatedEvent> record) {
        var event = record.value();
        log.info("Evento recebido: {}", event);
        processEventsUseCase.processEligibility(event.insuranceId());
    }
}
