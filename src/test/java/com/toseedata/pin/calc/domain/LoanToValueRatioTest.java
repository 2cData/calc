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
 * LTC Ratio - Loan amount / Value of Collateral
 */
public class LoanToValueRatioTest {

    @Autowired
    private LoanToValueRatio loanToValueRatio;

    long fairMarketValue = 100;
    MonetaryAmount fairMarketValueAmount = FastMoney.of(fairMarketValue, CURRENCY_CODE);

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void testInvalidLoanAmount() {
        MonetaryAmount loanAmount = FastMoney.of(BigDecimal.ZERO, CURRENCY_CODE);
        MonetaryAmount fmv = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new LoanToValueRatio(loanAmount, fmv));
    }

    @Test
    void testNullLoanAmount() {
        MonetaryAmount fmv = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);

        Assertions.assertThrows(NullPointerException.class, () ->
                new LoanToValueRatio(null, fmv));
    }

    @Test
    void testInvalidFMV() {
        MonetaryAmount loanAmount = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        MonetaryAmount fmv = FastMoney.of(BigDecimal.ZERO, CURRENCY_CODE);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new LoanToValueRatio(loanAmount, fmv));
    }

    @Test
    void testNullFMV() {
        MonetaryAmount loanAmount = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        Assertions.assertThrows(NullPointerException.class, () ->
                new LoanToValueRatio(loanAmount, null));
    }


    @Test
    void calculateLoanToValueRatioPositive() {
        // Given
        // a $100 loan

        // When
        // fair market value of collateral is 10x the loan amount
        MonetaryAmount loanAmount = fairMarketValueAmount.multiply(10);

        // Then
        // loan to value ratio is 100 / 10 = 10
        loanToValueRatio = new LoanToValueRatio(loanAmount, fairMarketValueAmount);

        MonetaryAmount result = FastMoney.of(10, "USD");

        assertEquals(
                LoanToValueRatio.calculate(), result);
    }

    @Test
    void calculateLoanToValueRatioNegative() {
        // Given
        // a $100 investment

        // When
        // fair market value of collateral is 1/10th the loan amount
        MonetaryAmount loanAmount = fairMarketValueAmount.divide(10);

        // Then
        // cash on cash return is 100 / 1000 = .1
        loanToValueRatio = new LoanToValueRatio(loanAmount, fairMarketValueAmount);

        MonetaryAmount result = FastMoney.of(.1, CURRENCY_CODE);

        assertEquals(
                LoanToValueRatio.calculate(), result);
    }

    @Test
    void calculateLoanToValueRatioNeutral() {
        // Given
        // a $100 investment

        // When
        // // fair market value of collateral is equal to the loan amount
        MonetaryAmount loanAmount = fairMarketValueAmount;

        // Then
        // cash on cash return is 100 / 100 = 1
        loanToValueRatio = new LoanToValueRatio(loanAmount, fairMarketValueAmount);

        MonetaryAmount result = FastMoney.of(1, CURRENCY_CODE);

        assertEquals(
                LoanToValueRatio.calculate(), result);
    }
}
