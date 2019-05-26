package com.toseedata.pin.calc.domain;

import org.javamoney.moneta.FastMoney;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

import static com.toseedata.pin.calc.config.Constants.CURRENCY_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
public class EquityBuildUpRateTest {

    @Autowired
    EquityBuildUpRate equityBuildUpRate;

    long yearOneOneMortgagePrincipalPaid = 100;
    MonetaryAmount yearOneOneMortgagePrincipalPaidAmount = FastMoney.of(yearOneOneMortgagePrincipalPaid, CURRENCY_CODE);

    @Test
    void testInvalidYearOneMortgagePrincipalPaid() {
        MonetaryAmount yearOneMortgagePrincipalPaid = FastMoney.of(BigDecimal.ZERO, CURRENCY_CODE);
        MonetaryAmount yearOneInitialCashInvested = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new EquityBuildUpRate(yearOneMortgagePrincipalPaid, yearOneInitialCashInvested));
    }

    @Test
    void testNullYearOneMortgagePrincipalPaid() {
        MonetaryAmount yearOneInitialCashInvested = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);

        Assertions.assertThrows(NullPointerException.class, () ->
                new EquityBuildUpRate(null, yearOneInitialCashInvested));
    }

    @Test
    void testInvalidYearOneInitialCashInvested() {
        MonetaryAmount yearOneMortgagePrincipalPaid = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        MonetaryAmount yearOneInitialCashInvested = FastMoney.of(BigDecimal.ZERO, CURRENCY_CODE);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new EquityBuildUpRate(yearOneMortgagePrincipalPaid, yearOneInitialCashInvested));
    }

    @Test
    void testNullYearOneInitialCashInvested() {
        MonetaryAmount yearOneMortgagePrincipalPaid = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);

        Assertions.assertThrows(NullPointerException.class, () ->
                new EquityBuildUpRate(yearOneMortgagePrincipalPaid, null));
    }


    @Test
    void calculateEquityBuildUpRatePositive() {
        // Given
        // a $100 investment

        // When
        // expenses are 1/10th the investment
        MonetaryAmount yearOneInitialCashInvested = yearOneOneMortgagePrincipalPaidAmount.divide(10);

        // Then
        // cash on cash return is 100 / 10 = 10
        equityBuildUpRate = new EquityBuildUpRate(yearOneOneMortgagePrincipalPaidAmount, yearOneInitialCashInvested);

        MonetaryAmount result = FastMoney.of(10, "USD");

        assertEquals(
                EquityBuildUpRate.calculate(), result);
    }

    @Test
    void calculateEquityBuildUpRateNegative() {
        // Given
        // a $100 investment

        // When
        // expenses are 10x the investment
        MonetaryAmount yearOneInitialCashInvested = yearOneOneMortgagePrincipalPaidAmount.multiply(10);

        // Then
        // cash on cash return is 100 / 1000 = .1
        equityBuildUpRate = new EquityBuildUpRate(yearOneOneMortgagePrincipalPaidAmount, yearOneInitialCashInvested);

        MonetaryAmount result = FastMoney.of(.1, CURRENCY_CODE);

        assertEquals(
                EquityBuildUpRate.calculate(), result);
    }

    @Test
    void calculateEquityBuildUpRateNeutral() {
        // Given
        // a $100 investment

        // When
        // expenses are equal to the investment
        MonetaryAmount yearOneInitialCashInvested = yearOneOneMortgagePrincipalPaidAmount;

        // Then
        // cash on cash return is 100 / 100 = 1
        equityBuildUpRate = new EquityBuildUpRate(yearOneOneMortgagePrincipalPaidAmount, yearOneInitialCashInvested);

        MonetaryAmount result = FastMoney.of(1, CURRENCY_CODE);

        assertEquals(
                EquityBuildUpRate.calculate(), result);
    }

}
