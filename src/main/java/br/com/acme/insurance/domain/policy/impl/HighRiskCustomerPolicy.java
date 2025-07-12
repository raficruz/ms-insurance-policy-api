package br.com.acme.insurance.domain.policy.impl;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.policy.EligibilityPolicy;
import br.com.acme.insurance.shared.enums.CustomerType;
import br.com.acme.insurance.shared.enums.InsuranceCategory;

import java.math.BigDecimal;

import static br.com.acme.insurance.shared.util.NumericalUtils.isLessThanOrEqual;

public class HighRiskCustomerPolicy implements EligibilityPolicy {

    @Override
    public boolean isEligible(Insurance insurance) {
        if (!insurance.getCustomerType().equals(CustomerType.HIGH_RISK)) return false;
        BigDecimal insuredAmount = insurance.getInsuredAmount();
        return switch (insurance.getCategory()) {
            case AUTO -> isLessThanOrEqual(insuredAmount, new BigDecimal(250_000));
            case RESIDENTIAL -> isLessThanOrEqual(insuredAmount, new BigDecimal(150_000));
            default -> isLessThanOrEqual(insuredAmount, new BigDecimal(125_000));
        };
    }
}
