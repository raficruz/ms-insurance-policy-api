package br.com.acme.insurance.adapter.controller.api;

import br.com.acme.insurance.adapter.controller.dto.CreateInsuranceRequestInput;
import br.com.acme.insurance.adapter.controller.dto.CreateInsuranceRequestOutput;
import br.com.acme.insurance.adapter.controller.dto.InsuranceDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;


public interface InsuranceRequestApi {

    @Operation(summary = "Cria uma nova solicitação de apólice de seguro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    ResponseEntity<CreateInsuranceRequestOutput> create(CreateInsuranceRequestInput input);

    @Operation(summary = "Busca uma solicitação de apólice pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação encontrada"),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada")
    })
    ResponseEntity<InsuranceDetails> getById(UUID id);

    @Operation(summary = "Busca solicitações de apólice por ID do cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitações encontradas")
    })
    ResponseEntity<List<InsuranceDetails>> getByCustomerId(UUID customerId);

    @Operation(summary = "Cancela uma solicitação de apólice de seguro")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Solicitação cancelada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada")
    })
    ResponseEntity<Void> cancel(UUID id);
}
