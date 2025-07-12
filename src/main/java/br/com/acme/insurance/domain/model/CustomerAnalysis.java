package br.com.acme.insurance.domain.model;

import br.com.acme.insurance.shared.enums.CustomerType;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerAnalysis {

    private UUID insuranceId;
    private UUID customerId;
    private CustomerType customerType;
    private OffsetDateTime assessedAt;
    List<String> findings;
}
