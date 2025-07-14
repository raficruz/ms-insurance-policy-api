package br.com.acme.insurance.bdd.stepdefinitions;

import br.com.acme.insurance.adapter.controller.dto.CreateInsuranceRequestInput;
import br.com.acme.insurance.bdd.dto.ErrorResponse;
import br.com.acme.insurance.util.FixtureFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateInsuranceStepsDef {
    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;

    private CreateInsuranceRequestInput input;

    private UUID insuranceId;

    private UUID customerId;

    @Dado("que o cliente deseja criar uma nova apólice")
    public void cliente_deseja_criar_nova_apolice() {
        input = FixtureFactory.validCreateInsuranceRequestInput();
    }

    @Dado("que a requisição está com os campos obrigatorios nulos ou em branco")
    public void requisicaoComCamposObrigatoriosAusentes() {
        input = FixtureFactory.incompleteCreateInsuranceRequestInput();
    }

    @Dado("que o cliente deseja criar uma nova apólice sem assistances e coverages")
    public void cliente_deseja_criar_nova_apolice_sem_assistances_e_coverages() {
        input = FixtureFactory.validCreateInsuranceRequestInput();
        input.setAssistances(Collections.emptyList());
        input.setCoverages(Collections.emptyMap());
    }

    @Quando("enviar uma requisição POST")
    public void enviar_requisicao_post() {
        enviarRequisicao(input);
    }

    @Entao("a resposta deve ter o status {int}")
    public void resposta_deve_ter_status(int statusCode) {
        assertThat(response.getStatusCode().value()).isEqualTo(statusCode);
    }

    @E("que o campo totalMonthlyPremiumAmount é {double} e o campo insuredAmount é {double}")
    public void preencher_campos_com_valores_invalidos(double totalMonthlyPremiumAmount, double insuredAmount) {
        input.setTotalMonthlyPremiumAmount(new java.math.BigDecimal(totalMonthlyPremiumAmount));
        input.setInsuredAmount(new java.math.BigDecimal(insuredAmount));
    }

    @E("a resposta deve conter as mensagens de validação:")
    public void resposta_deve_conter_mensagens_de_range(DataTable dataTable) throws Exception {
        var expected = dataTable.asMaps(String.class, String.class);
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = mapper.readValue(response.getBody(), ErrorResponse.class);

        for (var row : expected) {
            String field = row.get("field");
            String message = row.get("message");
            boolean found = errorResponse.messages.stream()
                    .anyMatch(e -> e.field.equals(field) && e.message.equals(message));
            assertThat(found)
                    .withFailMessage("Esperava mensagem para campo %s: %s", field, message)
                    .isTrue();
        }
    }

    private void enviarRequisicao(CreateInsuranceRequestInput payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateInsuranceRequestInput> request = new HttpEntity<>(payload, headers);
        response = restTemplate.postForEntity("/v1/insurance-requests", request, String.class);
    }



    @Dado("que existe uma apólice válida cadastrada")
    public void existe_uma_apolice_valida_cadastrada() throws Exception {
        CreateInsuranceRequestInput input = FixtureFactory.validCreateInsuranceRequestInput();
        input.setCustomerId(UUID.fromString("adc56d77-348c-4bf0-908f-22d402ee715c"));
        input.setProductId(UUID.fromString("1b2da7cc-b367-4196-8a78-9cfeec21f587"));
        input.setCategory(br.com.acme.insurance.shared.enums.InsuranceCategory.AUTO);
        input.setSalesChannel("MOBILE");
        input.setPaymentMethod("CREDIT_CARD");
        input.setTotalMonthlyPremiumAmount(new java.math.BigDecimal("75.25"));
        input.setInsuredAmount(new java.math.BigDecimal("275000.5"));
        Map<String, java.math.BigDecimal> coverages = new LinkedHashMap<>();
        coverages.put("Roubo", new java.math.BigDecimal("100000.25"));
        coverages.put("Perda Total", new java.math.BigDecimal("100000.25"));
        coverages.put("Colisão com Terceiros", new java.math.BigDecimal("75000.0"));
        input.setCoverages(coverages);
        input.setAssistances(List.of("Guincho até 250km", "Troca de Óleo", "Chaveiro 24h"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateInsuranceRequestInput> request = new HttpEntity<>(input, headers);
        ResponseEntity<String> createResponse = restTemplate.postForEntity("/v1/insurance-requests", request, String.class);

        assertThat(createResponse.getStatusCode().value()).isEqualTo(201);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(createResponse.getBody());
        this.insuranceId = UUID.fromString(json.get("id").asText());
    }

    @Quando("solicito a apólice pelo seu ID")
    public void solicito_apolice_pelo_id() {
        response = restTemplate.getForEntity("/v1/insurance-requests/" + insuranceId, String.class);
    }

    @E("o corpo da resposta deve conter os campos:")
    public void corpo_resposta_deve_conter_campos(DataTable dataTable) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.getBody());
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            String campo = row.get("campo");
            String valor = row.get("valor");
            JsonNode node = json.get(campo);
            if ("null".equals(valor)) {
                assertThat(node.isNull()).isTrue();
            } else {
                assertThat(node.asText()).isEqualTo(valor);
            }
        }
    }

    @E("o campo coverages deve conter:")
    public void campo_coverages_deve_conter(DataTable dataTable) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.getBody());
        JsonNode coverages = json.get("coverages");
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            String cobertura = row.get("cobertura");
            String valor = row.get("valor");
            assertThat(coverages.has(cobertura)).isTrue();
            assertThat(coverages.get(cobertura).asText()).isEqualTo(valor);
        }
    }

    @E("o campo assistances deve conter:")
    public void campo_assistances_deve_conter(DataTable dataTable) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.getBody());
        JsonNode assistances = json.get("assistances");
        List<String> expected = new ArrayList<>();
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            expected.add(row.get("assistência"));
        }
        List<String> actual = new ArrayList<>();
        assistances.forEach(a -> actual.add(a.asText()));
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @E("o campo history deve ser uma lista vazia")
    public void campo_history_lista_vazia() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.getBody());
        JsonNode history = json.get("history");
        assertThat(history.isArray()).isTrue();
        assertThat(history).isEmpty();
    }

    @Dado("que não existe apólice cadastrada com o ID {string}")
    public void nao_existe_apolice_com_id(String id) {
        this.insuranceId = UUID.fromString(id);
    }

    @Quando("solicito a apólice pelo seu ID inexistente")
    public void solicito_apolice_pelo_id_inexistente() {
        response = restTemplate.getForEntity("/v1/insurance-requests/" + insuranceId, String.class);
    }

    @E("a resposta deve conter a mensagem de erro:")
    public void resposta_deve_conter_mensagem_de_erro(DataTable dataTable) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.getBody());
        List<String> expected = new ArrayList<>();
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            String msg = row.get("mensagem");
            if (msg.contains("{id}")) {
                msg = msg.replace("{id}", insuranceId.toString());
            }
            expected.add(msg);
        }
        JsonNode messages = json.get("messages");
        List<String> actual = new ArrayList<>();
        messages.forEach(m -> actual.add(m.asText()));
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Dado("que existe uma ou mais apólices cadastradas para o customerId {string}")
    public void existe_apolices_para_customerId(String customerIdStr) {
        this.customerId = UUID.fromString(customerIdStr);

        CreateInsuranceRequestInput input = FixtureFactory.validCreateInsuranceRequestInput();
        input.setCustomerId(this.customerId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateInsuranceRequestInput> request = new HttpEntity<>(input, headers);
        ResponseEntity<String> createResponse = restTemplate.postForEntity("/v1/insurance-requests", request, String.class);
        assertThat(createResponse.getStatusCode().value()).isEqualTo(201);
    }

    @Dado("que não existe apólice cadastrada para o customerId {string}")
    public void nao_existe_apolice_para_customerId(String customerIdStr) {
        this.customerId = UUID.fromString(customerIdStr);
    }

    @Quando("solicito as apólices pelo customerId {string}")
    public void solicito_apolices_por_customerId(String customerIdStr) {
        response = restTemplate.getForEntity("/v1/insurance-requests?customer_id=" + customerIdStr, String.class);
    }

    @Entao("o corpo da resposta deve conter uma lista de apólices")
    public void corpo_deve_conter_lista_apolices() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.getBody());
        assertThat(json.isArray()).isTrue();
        assertThat(json.size()).isGreaterThan(0);

        for (JsonNode apolice : json) {
            assertThat(apolice.get("customerId").asText()).isEqualTo(customerId.toString());
        }
    }

    @Quando("solicito o cancelamento da apólice pelo seu ID")
    public void solicito_cancelamento_apolice_pelo_id() {
        ResponseEntity<String> resp = restTemplate.exchange(
                "/v1/insurance-requests/" + insuranceId,
                org.springframework.http.HttpMethod.DELETE,
                null,
                String.class
        );
        this.response = resp;
    }

    @E("o campo history deve conter o status cancelado")
    public void campo_history_deve_conter_status_cancelado() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.getBody());
        JsonNode history = json.get("history");
        assertThat(history.isArray()).isTrue();
        boolean found = false;
        for (JsonNode h : history) {
            if ("CANCELED".equals(h.get("status").asText())) {
                found = true;
                break;
            }
        }
        assertThat(found).isTrue();
    }
/****/
@Dado("que o cliente criou uma nova apólice")
public void cliente_criou_nova_apolice() throws Exception {
    input = FixtureFactory.validCreateInsuranceRequestInput();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<CreateInsuranceRequestInput> request = new HttpEntity<>(input, headers);
    ResponseEntity<String> createResponse = restTemplate.postForEntity("/v1/insurance-requests", request, String.class);
    assertThat(createResponse.getStatusCode().value()).isEqualTo(201);

    ObjectMapper mapper = new ObjectMapper();
    JsonNode json = mapper.readTree(createResponse.getBody());
    this.insuranceId = UUID.fromString(json.get("id").asText());
}

    @Quando("o cliente solicita o cancelamento da apólice")
    public void cliente_solicita_cancelamento_apolice() {
        ResponseEntity<String> cancelResponse = restTemplate.exchange(
                "/v1/insurance-requests/" + insuranceId,
                org.springframework.http.HttpMethod.DELETE,
                null,
                String.class
        );
        assertThat(cancelResponse.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Quando("o cliente busca a apólice pelo seu ID")
    public void cliente_busca_apolice_pelo_id() {
        response = restTemplate.getForEntity("/v1/insurance-requests/" + insuranceId, String.class);
    }

    @Entao("a resposta da busca deve ter o status {int}")
    public void resposta_busca_deve_ter_status(int statusCode) {
        assertThat(response.getStatusCode().value()).isEqualTo(statusCode);
    }

    @E("o corpo da resposta deve indicar status cancelado")
    public void corpo_resposta_deve_indicar_status_cancelado() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.getBody());
        assertThat(json.get("status").asText()).isEqualTo("CANCELED");
    }
}