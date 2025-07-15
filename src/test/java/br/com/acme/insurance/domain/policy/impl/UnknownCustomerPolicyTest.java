package br.com.acme.insurance.domain.policy.impl;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.shared.enums.CustomerType;
import br.com.acme.insurance.shared.enums.InsuranceCategory;
import br.com.acme.insurance.shared.enums.InsuranceStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UnknownCustomerPolicyTest {

    private final UnknownCustomerPolicy policy = new UnknownCustomerPolicy();

    private Insurance insurance(CustomerType type, InsuranceCategory category, InsuranceStatus status, BigDecimal amount) {
        return Insurance.builder()
                .id(UUID.randomUUID())
                .customerType(type)
                .category(category)
                .status(status)
                .insuredAmount(amount)
                .build();
    }

    @Test
    void retornaFalseQuandoStatusCancelado() {
        Insurance insurance = insurance(CustomerType.UNKNOWN, InsuranceCategory.LIFE, InsuranceStatus.CANCELED, new BigDecimal("200000"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseQuandoNaoEhUnknown() {
        Insurance insurance = insurance(CustomerType.REGULAR, InsuranceCategory.LIFE, InsuranceStatus.RECEIVED, new BigDecimal("200000"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaLifeAte200mil() {
        Insurance insurance = insurance(CustomerType.UNKNOWN, InsuranceCategory.LIFE, InsuranceStatus.RECEIVED, new BigDecimal("200000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaLifeAcima200mil() {
        Insurance insurance = insurance(CustomerType.UNKNOWN, InsuranceCategory.LIFE, InsuranceStatus.RECEIVED, new BigDecimal("200001"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaResidentialAte200mil() {
        Insurance insurance = insurance(CustomerType.UNKNOWN, InsuranceCategory.RESIDENTIAL, InsuranceStatus.RECEIVED, new BigDecimal("200000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaResidentialAcima200mil() {
        Insurance insurance = insurance(CustomerType.UNKNOWN, InsuranceCategory.RESIDENTIAL, InsuranceStatus.RECEIVED, new BigDecimal("200001"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaAutoAte75mil() {
        Insurance insurance = insurance(CustomerType.UNKNOWN, InsuranceCategory.AUTO, InsuranceStatus.RECEIVED, new BigDecimal("75000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaAutoAcima75mil() {
        Insurance insurance = insurance(CustomerType.UNKNOWN, InsuranceCategory.AUTO, InsuranceStatus.RECEIVED, new BigDecimal("75001"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaOtherAte55mil() {
        Insurance insurance = insurance(CustomerType.UNKNOWN, InsuranceCategory.OTHER, InsuranceStatus.RECEIVED, new BigDecimal("55000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaOtherAcima55mil() {
        Insurance insurance = insurance(CustomerType.UNKNOWN, InsuranceCategory.OTHER, InsuranceStatus.RECEIVED, new BigDecimal("55001"));
        assertFalse(policy.isEligible(insurance));
    }
}