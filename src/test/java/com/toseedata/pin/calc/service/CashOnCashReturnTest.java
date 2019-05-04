package com.toseedata.pin.calc.service;

import com.toseedata.pin.calc.domain.Amount;
import org.javamoney.moneta.FastMoney;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

//import org.junit.platform.runner.JUnitPlatform;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(JUnitPlatform.class)
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@RunWith(JUnitPlatform.class)
//@ExtendWith(SpringExtension.class)
public class CashOnCashReturnTest {
    String currencyCode = "USD";
    long yearOneCashFlow = 100;
    Amount yearOneCashFlowAmount = new Amount(new BigDecimal(yearOneCashFlow), currencyCode);
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
        Amount yearOneCashFlow = new Amount(new BigDecimal(BigInteger.ZERO), currencyCode);
        Amount yearOneCapitalExpense = new Amount(new BigDecimal(BigInteger.ONE), currencyCode);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CashOnCashReturn(yearOneCashFlow, yearOneCapitalExpense));
    }

    @Test
    void testNullYearOneCashFlow() {
        Amount yearOneCapitalExpense = new Amount(new BigDecimal(BigInteger.ONE), currencyCode);

        Assertions.assertThrows(NullPointerException.class, () ->
                new CashOnCashReturn(null, yearOneCapitalExpense));
    }

    @Test
    void testInvalidYearOneCapitalExpenses() {
        Amount yearOneCashFlow = new Amount(new BigDecimal(BigInteger.ONE), currencyCode);
        Amount yearOneCapitalExpense = new Amount(new BigDecimal(BigInteger.ZERO), currencyCode);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CashOnCashReturn(yearOneCashFlow, yearOneCapitalExpense));
    }

    @Test
    void testNullYearOneCapitalExpenses() {
        Amount yearOneCashFlow = new Amount(new BigDecimal(BigInteger.ONE), currencyCode);

        Assertions.assertThrows(NullPointerException.class, () ->
                new CashOnCashReturn(yearOneCashFlow, null));
    }

    @Test
    void calculateCashOnCashReturnPositive() {
        // Given
        // a $100 investment

        // When
        // expenses are 1/10th the investment
        Amount yearOneCapitalExpensesAmount = new Amount(new BigDecimal(yearOneCashFlow / 10), currencyCode);

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
        Amount yearOneCapitalExpensesAmount = new Amount(new BigDecimal(yearOneCashFlow * 10), currencyCode);

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
        Amount yearOneCapitalExpensesAmount = new Amount(new BigDecimal(yearOneCashFlow), currencyCode);

        // Then
        // cash on cash return is 100 / 100 = 1
        cashOnCashReturn = new CashOnCashReturn(yearOneCashFlowAmount, yearOneCapitalExpensesAmount);

        MonetaryAmount result = FastMoney.of(1, "USD");

        assertEquals(
                CashOnCashReturn.calculate(), result);
    }

}