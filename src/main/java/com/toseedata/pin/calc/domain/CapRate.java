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
public final class CapRate {
    private static MonetaryAmount netOperatingIncome;
    private static MonetaryAmount fairMarketValue;

    /**
     * @param netOperatingIncome
     * @param fairMarketValue
     */
    public CapRate(@Nonnull final MonetaryAmount netOperatingIncome, @Nonnull final MonetaryAmount fairMarketValue) {
        checkArgument(!netOperatingIncome.isNegativeOrZero(), "Net Operating Income must be greater than zero");
        checkArgument(!fairMarketValue.isNegativeOrZero(), "Fair Market Value must be greater than zero");

        checkNotNull(netOperatingIncome, "Net Operating Income must not be null");
        checkNotNull(fairMarketValue, "Fair Market Value must not be null");

        CapRate.netOperatingIncome = netOperatingIncome;
        CapRate.fairMarketValue = fairMarketValue;
    }

    static BigDecimal calculate() {
        BigDecimal noi = new BigDecimal(netOperatingIncome.getNumber().doubleValueExact());
        BigDecimal fmv = new BigDecimal(fairMarketValue.getNumber().doubleValueExact());

        BigDecimal result = noi.divide(fmv,4,RoundingMode.HALF_UP);

        assert (!result.equals(BigDecimal.ZERO));

        return result.stripTrailingZeros();
    }
}
