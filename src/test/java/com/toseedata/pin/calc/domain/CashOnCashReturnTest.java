package com.toseedata.pin.calc.domain;

import org.javamoney.moneta.FastMoney;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CashOnCashReturnTest {
    String currencyCode = "USD";
    long yearOneCashFlow = 100;
    MonetaryAmount yearOneCashFlowAmount = FastMoney.of(yearOneCashFlow, currencyCode);

    @Autowired
    private CashOnCashReturn cashOnCashReturn;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void testInvalidYearOneCashFlow() {
        MonetaryAmount yearOneCashFlow = FastMoney.of(BigDecimal.ZERO, currencyCode);
        MonetaryAmount yearOneCapitalExpense = FastMoney.of(BigDecimal.ONE, currencyCode);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CashOnCashReturn(yearOneCashFlow, yearOneCapitalExpense));
    }

    @Test
    void testNullYearOneCashFlow() {
        MonetaryAmount yearOneCapitalExpense = FastMoney.of(BigDecimal.ONE, currencyCode);

        Assertions.assertThrows(NullPointerException.class, () ->
                new CashOnCashReturn(null, yearOneCapitalExpense));
    }

    @Test
    void testInvalidYearOneCapitalExpenses() {
        MonetaryAmount yearOneCashFlow = FastMoney.of(BigDecimal.ONE, currencyCode);
        MonetaryAmount yearOneCapitalExpense = FastMoney.of(BigDecimal.ZERO, currencyCode);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CashOnCashReturn(yearOneCashFlow, yearOneCapitalExpense));
    }

    @Test
    void testNullYearOneCapitalExpenses() {
        MonetaryAmount yearOneCashFlow = FastMoney.of(BigDecimal.ONE, currencyCode);

        Assertions.assertThrows(NullPointerException.class, () ->
                new CashOnCashReturn(yearOneCashFlow, null));
    }

    @Test
    void calculateCashOnCashReturnPositive() {
        // Given
        // a $100 investment

        // When
        // expenses are 1/10th the investment
        MonetaryAmount yearOneCapitalExpensesAmount = yearOneCashFlowAmount.divide(10);

        // Then
        // cash on cash return is 100 / 10 = 10
        cashOnCashReturn = new CashOnCashReturn(yearOneCashFlowAmount, yearOneCapitalExpensesAmount);

        MonetaryAmount result = FastMoney.of(10, "USD");

        assertEquals(
                CashOnCashReturn.calculate(), result);
    }

    @Test
    void calculateCashOnCashReturnNegative() {
        // Given
        // a $100 investment

        // When
        // expenses are 10x the investment
        MonetaryAmount yearOneCapitalExpensesAmount = yearOneCashFlowAmount.multiply(10);

        // Then
        // cash on cash return is 100 / 1000 = .1
        cashOnCashReturn = new CashOnCashReturn(yearOneCashFlowAmount, yearOneCapitalExpensesAmount);

        MonetaryAmount result = FastMoney.of(.1, "USD");

        assertEquals(
                CashOnCashReturn.calculate(), result);
    }

    @Test
    void calculateCashOnCashReturnNeutral() {
        // Given
        // a $100 investment

        // When
        // expenses are equal to the investment
        MonetaryAmount yearOneCapitalExpensesAmount = yearOneCashFlowAmount;

        // Then
        // cash on cash return is 100 / 100 = 1
        cashOnCashReturn = new CashOnCashReturn(yearOneCashFlowAmount, yearOneCapitalExpensesAmount);

        MonetaryAmount result = FastMoney.of(1, "USD");

        assertEquals(
                CashOnCashReturn.calculate(), result);
    }

}