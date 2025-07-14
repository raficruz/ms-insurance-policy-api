package br.com.acme.insurance.util;

import br.com.acme.insurance.adapter.controller.dto.CreateInsuranceRequestInput;
import br.com.acme.insurance.shared.enums.InsuranceCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

public class FixtureFactory {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static CreateInsuranceRequestInput fromJson(String fileName) {
        try (InputStream is = FixtureFactory.class.getResourceAsStream("/fixtures/" + fileName)) {
            return objectMapper.readValue(is, CreateInsuranceRequestInput.class);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler fixture: " + fileName, e);
        }
    }
    public static CreateInsuranceRequestInput validCreateInsuranceRequestInput() {
        return fromJson("valid-create-insurance-request.json");
    }

    public static CreateInsuranceRequestInput invalidCreateInsuranceRequestInput() {
        return fromJson("invalid-create-insurance-request.json");
    }

    public static CreateInsuranceRequestInput incompleteCreateInsuranceRequestInput() {
        return fromJson("incomplete-create-insurance-request.json");
    }

    public static CreateInsuranceRequestInput invalidCustomerIdInsuranceRequestInput() {
        return new CreateInsuranceRequestInput(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                UUID.randomUUID(),
                InsuranceCategory.LIFE,
                "ONLINE",
                "CREDIT_CARD",
                new BigDecimal("100.00"),
                new BigDecimal("25000.00"),
                Collections.emptyMap(),
                Collections.emptyList()
        );
    }

    public static ResponseEntity<String> sendMalformedUUIDRequest(TestRestTemplate restTemplate) {
        String json = """
            {
              "customerId": "malformed-uuid",
              "productId": "also-malformed",
              "category": "LIFE",
              "salesChannel": "ONLINE",
              "paymentMethod": "CREDIT_CARD",
              "totalMonthlyPremiumAmount": 150.0,
              "insuredAmount": 50000.0,
              "coverages": [],
              "assistances": []
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return restTemplate.postForEntity("/v1/insurance-requests", request, String.class);
    }

    public static ResponseEntity<String> sendInvalidCategoryRequest(TestRestTemplate restTemplate) {
        String json = """
            {
              "customerId": "%s",
              "productId": "%s",
              "category": "INVALID_CATEGORY",
              "salesChannel": "ONLINE",
              "paymentMethod": "CREDIT_CARD",
              "totalMonthlyPremiumAmount": 150.0,
              "insuredAmount": 50000.0,
              "coverages": [],
              "assistances": []
            }
            """.formatted(UUID.randomUUID(), UUID.randomUUID());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return restTemplate.postForEntity("/v1/insurance-requests", request, String.class);
    }
}
