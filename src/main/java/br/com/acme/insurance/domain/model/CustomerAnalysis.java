package br.com.acme.insurance.domain.model;

import br.com.acme.insurance.shared.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class CustomerAnalysis {

    private UUID insuranceId;
    private UUID customerId;
    private CustomerType customerType;
    private OffsetDateTime assessedAt;
    List<String> findings;
}
