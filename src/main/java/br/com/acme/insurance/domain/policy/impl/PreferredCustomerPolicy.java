package br.com.acme.insurance.domain.policy.impl;

import br.com.acme.insurance.domain.model.Insurance;
import br.com.acme.insurance.domain.policy.EligibilityPolicy;
import br.com.acme.insurance.shared.enums.CustomerType;
import br.com.acme.insurance.shared.enums.InsuranceCategory;

import java.math.BigDecimal;

import static br.com.acme.insurance.shared.util.NumericalUtils.isLessThanOrEqual;

public class PreferredCustomerPolicy implements EligibilityPolicy {

    @Override
    public boolean isEligible(Insurance insurance) {
        if (!insurance.getCustomerType().equals(CustomerType.PREFERRED)) return false;
        BigDecimal insuredAmount = insurance.getInsuredAmount();
        return switch (insurance.getCategory()) {
            case LIFE -> isLessThanOrEqual(insuredAmount, new BigDecimal(800_000));
            case AUTO, RESIDENTIAL -> isLessThanOrEqual(insuredAmount, new BigDecimal(450_000));
            default -> isLessThanOrEqual(insuredAmount, new BigDecimal(375_000));
        };
    }
}
