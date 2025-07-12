package br.com.acme.insurance.domain.policy;

import br.com.acme.insurance.domain.model.Insurance;

public interface EligibilityPolicy {
    boolean isEligible(Insurance insurance);
}
