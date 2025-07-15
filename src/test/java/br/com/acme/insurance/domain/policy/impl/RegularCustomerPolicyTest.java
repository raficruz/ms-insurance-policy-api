package br.com.acme.insurance.domain.policy.impl;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.shared.enums.CustomerType;
import br.com.acme.insurance.shared.enums.InsuranceCategory;
import br.com.acme.insurance.shared.enums.InsuranceStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RegularCustomerPolicyTest {

    private final RegularCustomerPolicy policy = new RegularCustomerPolicy();

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
        Insurance insurance = insurance(CustomerType.REGULAR, InsuranceCategory.LIFE, InsuranceStatus.CANCELED, new BigDecimal("500000"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseQuandoNaoEhRegular() {
        Insurance insurance = insurance(CustomerType.PREFERRED, InsuranceCategory.LIFE, InsuranceStatus.RECEIVED, new BigDecimal("500000"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaLifeAte500mil() {
        Insurance insurance = insurance(CustomerType.REGULAR, InsuranceCategory.LIFE, InsuranceStatus.RECEIVED, new BigDecimal("500000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaLifeAcima500mil() {
        Insurance insurance = insurance(CustomerType.REGULAR, InsuranceCategory.LIFE, InsuranceStatus.RECEIVED, new BigDecimal("500001"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaResidentialAte500mil() {
        Insurance insurance = insurance(CustomerType.REGULAR, InsuranceCategory.RESIDENTIAL, InsuranceStatus.RECEIVED, new BigDecimal("500000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaResidentialAcima500mil() {
        Insurance insurance = insurance(CustomerType.REGULAR, InsuranceCategory.RESIDENTIAL, InsuranceStatus.RECEIVED, new BigDecimal("500001"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaAutoAte350mil() {
        Insurance insurance = insurance(CustomerType.REGULAR, InsuranceCategory.AUTO, InsuranceStatus.RECEIVED, new BigDecimal("350000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaAutoAcima350mil() {
        Insurance insurance = insurance(CustomerType.REGULAR, InsuranceCategory.AUTO, InsuranceStatus.RECEIVED, new BigDecimal("350001"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaOtherAte255mil() {
        Insurance insurance = insurance(CustomerType.REGULAR, InsuranceCategory.OTHER, InsuranceStatus.RECEIVED, new BigDecimal("255000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaOtherAcima255mil() {
        Insurance insurance = insurance(CustomerType.REGULAR, InsuranceCategory.OTHER, InsuranceStatus.RECEIVED, new BigDecimal("255001"));
        assertFalse(policy.isEligible(insurance));
    }
}