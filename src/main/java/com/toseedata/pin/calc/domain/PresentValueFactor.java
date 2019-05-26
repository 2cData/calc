package com.toseedata.pin.calc.domain;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.toseedata.pin.calc.config.Constants.DECIMAL_SCALE;

public final class PresentValueFactor {

    private static Integer numberOfPeriods;
    private static BigDecimal rate;

    public PresentValueFactor(@Nonnull final Integer numberOfPeriods, @Nonnull final BigDecimal rate) {
        checkArgument(numberOfPeriods.compareTo(0) > 0, "Number Of Periods must be greater than zero");
        checkArgument(rate.compareTo(BigDecimal.ZERO) > 0, "Rate must be greater than zero");

        checkNotNull(numberOfPeriods, "Number Of Periods must not be null");
        checkNotNull(rate, "Rate must not be null");

        this.numberOfPeriods = numberOfPeriods;
        this.rate = rate;
    }

    /**
     * PV Factor = 1 / (1+r)^n
     */
    static BigDecimal calculate() {
        // (1+r)
        BigDecimal stepOne = BigDecimal.ONE.add(rate);

        // (1+r)^n
        BigDecimal stepTwo = stepOne.pow(numberOfPeriods);

        // 1 / (1+r)^n
        BigDecimal stepThree = BigDecimal.ONE.divide(stepTwo, DECIMAL_SCALE, RoundingMode.HALF_UP);

        assert(stepThree.compareTo(BigDecimal.ZERO)>0);

        return stepThree.stripTrailingZeros();
    }
}
