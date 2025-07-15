package br.com.acme.insurance.infrastructure.database.entity;

import br.com.acme.insurance.shared.enums.InsuranceStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.Instant;
import java.time.OffsetDateTime;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatusChangeEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InsuranceStatus status;

    @Column(name = "timestamp", nullable = false)
    private OffsetDateTime timestamp;
}
