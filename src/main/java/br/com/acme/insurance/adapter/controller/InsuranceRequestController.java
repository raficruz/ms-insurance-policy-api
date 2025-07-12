package br.com.acme.insurance.adapter.controller;

import br.com.acme.insurance.adapter.controller.api.InsuranceRequestApi;
import br.com.acme.insurance.adapter.controller.dto.CreateInsuranceRequestInput;
import br.com.acme.insurance.adapter.controller.dto.CreateInsuranceRequestOutput;
import br.com.acme.insurance.adapter.controller.dto.InsuranceDetails;
import br.com.acme.insurance.adapter.controller.mapper.InsuranceDtoMapper;
import br.com.acme.insurance.application.usecase.CancelInsuranceUseCase;
import br.com.acme.insurance.application.usecase.CreateInsuranceUseCase;
import br.com.acme.insurance.application.usecase.GetInsuranceUseCase;
import br.com.acme.insurance.domain.model.Insurance;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/insurance-requests")
public class InsuranceRequestController implements InsuranceRequestApi {

    private final CreateInsuranceUseCase createInsuranceUseCase;
    private final GetInsuranceUseCase getInsuranceUseCase;
    private final CancelInsuranceUseCase cancelInsuranceUseCase;
    private final InsuranceDtoMapper mapper;

    public InsuranceRequestController(
            CreateInsuranceUseCase createInsuranceUseCase,
            GetInsuranceUseCase getInsuranceUseCase,
            CancelInsuranceUseCase cancelInsuranceUseCase,
            InsuranceDtoMapper mapper
    ) {
        this.createInsuranceUseCase = createInsuranceUseCase;
        this.getInsuranceUseCase = getInsuranceUseCase;
        this.cancelInsuranceUseCase = cancelInsuranceUseCase;
        this.mapper = mapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<CreateInsuranceRequestOutput> create(@RequestBody @Valid CreateInsuranceRequestInput input) {
        Insurance insurance = mapper.toDomain(input);
        UUID id = createInsuranceUseCase.execute(insurance);
        return ResponseEntity
                .status(201)
                .body(new CreateInsuranceRequestOutput(id, insurance.getCreatedAt()));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<InsuranceDetails> getById(@PathVariable UUID id) {
        return getInsuranceUseCase.byId(id)
                .map(mapper::toDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @GetMapping
    public ResponseEntity<List<InsuranceDetails>> getByCustomerId(@RequestParam("customer_id") UUID customerId) {
        List<InsuranceDetails> list = getInsuranceUseCase.byCustomerId(customerId)
                .stream()
                .map(mapper::toDetails)
                .toList();

        return ResponseEntity.ok(list);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        cancelInsuranceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
