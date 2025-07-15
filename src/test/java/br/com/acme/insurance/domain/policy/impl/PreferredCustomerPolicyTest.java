package br.com.acme.insurance.domain.policy.impl;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.shared.enums.CustomerType;
import br.com.acme.insurance.shared.enums.InsuranceCategory;
import br.com.acme.insurance.shared.enums.InsuranceStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PreferredCustomerPolicyTest {

    private final PreferredCustomerPolicy policy = new PreferredCustomerPolicy();

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
        Insurance insurance = insurance(CustomerType.PREFERRED, InsuranceCategory.LIFE, InsuranceStatus.CANCELED, new BigDecimal("800000"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseQuandoNaoEhPreferred() {
        Insurance insurance = insurance(CustomerType.REGULAR, InsuranceCategory.LIFE, InsuranceStatus.RECEIVED, new BigDecimal("800000"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaLifeAte800mil() {
        Insurance insurance = insurance(CustomerType.PREFERRED, InsuranceCategory.LIFE, InsuranceStatus.RECEIVED, new BigDecimal("800000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaLifeAcima800mil() {
        Insurance insurance = insurance(CustomerType.PREFERRED, InsuranceCategory.LIFE, InsuranceStatus.RECEIVED, new BigDecimal("800001"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaAutoAte450mil() {
        Insurance insurance = insurance(CustomerType.PREFERRED, InsuranceCategory.AUTO, InsuranceStatus.RECEIVED, new BigDecimal("450000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaAutoAcima450mil() {
        Insurance insurance = insurance(CustomerType.PREFERRED, InsuranceCategory.AUTO, InsuranceStatus.RECEIVED, new BigDecimal("450001"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaResidentialAte450mil() {
        Insurance insurance = insurance(CustomerType.PREFERRED, InsuranceCategory.RESIDENTIAL, InsuranceStatus.RECEIVED, new BigDecimal("450000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaResidentialAcima450mil() {
        Insurance insurance = insurance(CustomerType.PREFERRED, InsuranceCategory.RESIDENTIAL, InsuranceStatus.RECEIVED, new BigDecimal("450001"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaOtherAte375mil() {
        Insurance insurance = insurance(CustomerType.PREFERRED, InsuranceCategory.OTHER, InsuranceStatus.RECEIVED, new BigDecimal("375000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaOtherAcima375mil() {
        Insurance insurance = insurance(CustomerType.PREFERRED, InsuranceCategory.OTHER, InsuranceStatus.RECEIVED, new BigDecimal("375001"));
        assertFalse(policy.isEligible(insurance));
    }
}