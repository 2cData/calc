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

public final class FutureValue {
    private static MonetaryAmount cashFlow;
    private static Integer durationYears;
    private static BigDecimal apr;
    private static CurrencyUnit currencyUnit;

    public FutureValue(@Nonnull final MonetaryAmount cashFlow,
                       @Nonnull final Integer durationYears,
                       @Nonnull final BigDecimal apr) {

        checkArguments(cashFlow, durationYears, apr);

        this.cashFlow = cashFlow;
        this.durationYears = durationYears;
        this.apr = apr;
        this.currencyUnit = Monetary.getCurrency(CURRENCY_CODE);
    }

    public FutureValue(@Nonnull final MonetaryAmount cashFlow,
                       @Nonnull final Integer durationYears,
                       @Nonnull final BigDecimal apr,
                       CurrencyUnit currencyUnit) {

        checkArguments(cashFlow, durationYears, apr);

        this.cashFlow = cashFlow;
        this.durationYears = durationYears;
        this.apr = apr;
        this.currencyUnit = currencyUnit;
    }

    /**
     * FV = C(1+r)^n
     *
     * @return
     */
    static MonetaryAmount calculate() {
        //FV = C(1+r)^n

        // (1+r)
        BigDecimal stepOne = BigDecimal.ONE.add(apr).setScale(DECIMAL_SCALE, RoundingMode.HALF_UP);

        // (1+r)^n
        BigDecimal stepTwo = stepOne.pow(durationYears).setScale(DECIMAL_SCALE , RoundingMode.HALF_UP);

        // C(1+r)^n
        MonetaryAmount stepThree = cashFlow.multiply(stepTwo);

        assert (!stepThree.isZero());

        return stepThree.stripTrailingZeros().getFactory().setCurrency(currencyUnit).create();
    }

    private void checkArguments(@Nonnull final MonetaryAmount cashFlow,
                                @Nonnull final Integer durationYears,
                                @Nonnull final BigDecimal apr) {
        checkArgument(!cashFlow.isNegativeOrZero(), "Cash Flow must be greater than zero");
        checkArgument(durationYears.compareTo(0) > 0, "Duration Years must be greater than zero");
        checkArgument(apr.compareTo(BigDecimal.ZERO) > 0, "APR must be greater than zero");

        checkNotNull(cashFlow, "Future Value must not be null");
        checkNotNull(durationYears, "Duration Years must not be null");
        checkNotNull(apr, "APR must not be null");
    }
}
