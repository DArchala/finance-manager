package pl.archala.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class BigDecimalProvider {

    private BigDecimalProvider() {}

    public static BigDecimal DEFAULT_VALUE = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

}
