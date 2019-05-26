package com.toseedata.pin.calc.domain;

import javax.annotation.Nonnull;
import javax.money.MonetaryAmount;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Equity Build Up Rate = Year 1 Mortgage Principal Paid / Year 1 Initial Cash Invested
 * <p>
 * Equity build up rate pairs perfectly with cash on cash return because they both calculate a source of returns in year 1.
 * Cash on cash return calculates a return on cash flow while equity build up rate calculates a return on principal payments.
 * When you add these two together, a total return in year 1 is the result.
 * <p>
 * These also pair well together because they are both only meant to be year 1 calculations
 * and are not to be used in subsequent years.
 * The reason is that they don't factor in the time value of money in the following years.
 * https://iqcalculators.com/real-estate-equity-build-up-rate
 */
public final class EquityBuildUpRate {

    private static MonetaryAmount yearOneMortgagePrincipalPaid;
    private static MonetaryAmount yearOneInitialCashInvested;

    public EquityBuildUpRate(@Nonnull final MonetaryAmount yearOneMortgagePrincipalPaid,
                             @Nonnull final MonetaryAmount yearOneInitialCashInvested) {

        checkArgument(!yearOneMortgagePrincipalPaid.isNegativeOrZero(), "Year One Mortgage Principal Paid must be greater than zero");
        checkArgument(!yearOneInitialCashInvested.isNegativeOrZero(), "Year One Initial Cash Invested must be greater than zero");
        checkNotNull(yearOneMortgagePrincipalPaid, "Year One Mortgage Principal Paid must not be null");
        checkNotNull(yearOneInitialCashInvested, "Year One Initial Cash Invested must not be null");

        EquityBuildUpRate.yearOneMortgagePrincipalPaid = yearOneMortgagePrincipalPaid;
        EquityBuildUpRate.yearOneInitialCashInvested = yearOneInitialCashInvested;
    }

    static MonetaryAmount calculate() {
        MonetaryAmount result =
                yearOneMortgagePrincipalPaid.divide(
                        yearOneInitialCashInvested.getNumber().doubleValueExact());

        assert (!result.isZero());

        return result.stripTrailingZeros();
    }
}
