package com.toseedata.pin.calc.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.money.MonetaryAmount;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Slf4j
public final class LoanToValueRatio {

    private static MonetaryAmount loanAmount;
    private static MonetaryAmount fairMarketValue;


    /**
     *
     * @param loanAmount
     * @param fairMarketValue
     */
    public LoanToValueRatio(@Nonnull final MonetaryAmount loanAmount, @Nonnull final MonetaryAmount fairMarketValue) {
        checkArgument(!loanAmount.isNegativeOrZero(), "loanAmount must be greater than zero");
        checkArgument(!fairMarketValue.isNegativeOrZero(), "fairMarketValue must be greater than zero");
        checkNotNull(loanAmount, "loanAmount must not be null");
        checkNotNull(fairMarketValue, "fairMarketValue must not be null");

        this.loanAmount = loanAmount;
        this.fairMarketValue = fairMarketValue;
    }

    static MonetaryAmount calculate() {
        MonetaryAmount result =
                loanAmount.divide(
                        fairMarketValue.getNumber().doubleValueExact());

        assert (!result.isZero());

        return result;
    }
}
