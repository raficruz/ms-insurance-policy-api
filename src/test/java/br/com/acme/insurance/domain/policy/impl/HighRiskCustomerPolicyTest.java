package br.com.acme.insurance.domain.policy.impl;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.shared.enums.CustomerType;
import br.com.acme.insurance.shared.enums.InsuranceCategory;
import br.com.acme.insurance.shared.enums.InsuranceStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HighRiskCustomerPolicyTest {

    private final HighRiskCustomerPolicy policy = new HighRiskCustomerPolicy();

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
        Insurance insurance = insurance(CustomerType.HIGH_RISK, InsuranceCategory.AUTO, InsuranceStatus.CANCELED, new BigDecimal("100000"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseQuandoNaoEhHighRisk() {
        Insurance insurance = insurance(CustomerType.PREFERRED, InsuranceCategory.AUTO, InsuranceStatus.RECEIVED, new BigDecimal("100000"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaAutoAte250mil() {
        Insurance insurance = insurance(CustomerType.HIGH_RISK, InsuranceCategory.AUTO, InsuranceStatus.RECEIVED, new BigDecimal("250000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaAutoAcima250mil() {
        Insurance insurance = insurance(CustomerType.HIGH_RISK, InsuranceCategory.AUTO, InsuranceStatus.RECEIVED, new BigDecimal("250001"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaResidentialAte150mil() {
        Insurance insurance = insurance(CustomerType.HIGH_RISK, InsuranceCategory.RESIDENTIAL, InsuranceStatus.RECEIVED, new BigDecimal("150000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaResidentialAcima150mil() {
        Insurance insurance = insurance(CustomerType.HIGH_RISK, InsuranceCategory.RESIDENTIAL, InsuranceStatus.RECEIVED, new BigDecimal("150001"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaLifeAte125mil() {
        Insurance insurance = insurance(CustomerType.HIGH_RISK, InsuranceCategory.LIFE, InsuranceStatus.RECEIVED, new BigDecimal("125000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaLifeAcima125mil() {
        Insurance insurance = insurance(CustomerType.HIGH_RISK, InsuranceCategory.LIFE, InsuranceStatus.RECEIVED, new BigDecimal("125001"));
        assertFalse(policy.isEligible(insurance));
    }

    @Test
    void retornaTrueParaOtherAte125mil() {
        Insurance insurance = insurance(CustomerType.HIGH_RISK, InsuranceCategory.OTHER, InsuranceStatus.RECEIVED, new BigDecimal("125000"));
        assertTrue(policy.isEligible(insurance));
    }

    @Test
    void retornaFalseParaOtherAcima125mil() {
        Insurance insurance = insurance(CustomerType.HIGH_RISK, InsuranceCategory.OTHER, InsuranceStatus.RECEIVED, new BigDecimal("125001"));
        assertFalse(policy.isEligible(insurance));
    }
}