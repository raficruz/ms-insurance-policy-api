package br.com.acme.insurance.shared.util;

import java.math.BigDecimal;

public class NumericalUtils {

    private NumericalUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isLessThanOrEqual(BigDecimal value, BigDecimal limit) {
        if (value == null || limit == null) {
            return false;
        }
        return value.compareTo(limit) <= 0;
    }
}
