package br.com.acme.insurance.infrastructure.messaging.publisher;

import br.com.acme.insurance.shared.enums.InsuranceStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InsuranceEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public InsuranceEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishReceivedEvent(UUID insuranceId) {
        kafkaTemplate.send("insurance-received", insuranceId.toString(), InsuranceStatus.RECEIVED.name());
    }
}