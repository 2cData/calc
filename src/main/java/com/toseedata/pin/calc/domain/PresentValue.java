package com.toseedata.pin.calc.domain;

import javax.annotation.Nonnull;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.toseedata.pin.calc.config.Constants.CURRENCY_CODE;
import static com.toseedata.pin.calc.config.Constants.DECIMAL_SCALE;

public final class PresentValue {
    private static MonetaryAmount futureValue;
    private static Integer durationYears;
    private static BigDecimal apr;
    private static CurrencyUnit currencyUnit;

    public PresentValue(@Nonnull final MonetaryAmount futureValue,
                        @Nonnull final Integer durationYears,
                        @Nonnull final BigDecimal apr) {

        checkArguments(futureValue, durationYears, apr);

        PresentValue.futureValue = futureValue;
        PresentValue.durationYears = durationYears;
        PresentValue.apr = apr;
        currencyUnit = Monetary.getCurrency(CURRENCY_CODE);
    }

    public PresentValue(@Nonnull final MonetaryAmount futureValue,
                        @Nonnull final Integer durationYears,
                        @Nonnull final BigDecimal apr,
                        CurrencyUnit currencyUnit) {

        checkArguments(futureValue, durationYears, apr);

        PresentValue.futureValue = futureValue;
        PresentValue.durationYears = durationYears;
        PresentValue.apr = apr;
        PresentValue.currencyUnit = currencyUnit;
    }

    /**
     * PV = FV[1/(1+i)n]
     *
     * @return
     */
    static MonetaryAmount calculate() {
        //PV = FV[1/(1+i)n]

        // (1+i)
        BigDecimal stepOne = BigDecimal.ONE.add(apr).setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);

        // (1+i)n
        BigDecimal stepTwo = stepOne.pow(durationYears).setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);

        // 1/(1+i)n
        BigDecimal stepThree = BigDecimal.ONE.divide(stepTwo, DECIMAL_SCALE, RoundingMode.HALF_UP);

        //FV[1/(1+i)n]
        MonetaryAmount stepFour = futureValue.multiply(stepThree);

        assert (!stepFour.isZero());

        return stepFour.stripTrailingZeros().getFactory().setCurrency(currencyUnit).create();
    }

    private void checkArguments(@Nonnull final MonetaryAmount futureValue,
                                @Nonnull final Integer durationYears,
                                @Nonnull final BigDecimal apr) {
        checkArgument(!futureValue.isNegativeOrZero(), "Future Value must be greater than zero");
        checkArgument(durationYears.compareTo(0) > 0, "Duration Years must be greater than zero");
        checkArgument(apr.compareTo(BigDecimal.ZERO) > 0, "APR must be greater than zero");

        checkNotNull(futureValue, "Future Value must not be null");
        checkNotNull(durationYears, "Duration Years must not be null");
        checkNotNull(apr, "APR must not be null");
    }
}
