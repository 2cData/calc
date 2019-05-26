package com.toseedata.pin.calc.domain;

import org.javamoney.moneta.FastMoney;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.toseedata.pin.calc.config.Constants.CURRENCY_CODE;
import static com.toseedata.pin.calc.config.Constants.DECIMAL_SCALE;

/**
 * FV = C(1+r)^n
 * <p>
 * C = Cash Flow at period 0 (ex $100)
 * r = Rate of return (ex 0.08)
 * n = number of periods (1 is a year assuming the rate of return is an annual rate of return)
 * FV = 108
 */
public class FutureValueTest {

    @Autowired
    private FutureValue futureValue;

    long cashFlowAmount = 100;
    MonetaryAmount cashFlow = FastMoney.of(cashFlowAmount, CURRENCY_CODE);
    Integer durationYears = 2;
    BigDecimal apr = new BigDecimal(.08);

    @Test
    public void testInvalidCashFlow() {

        MonetaryAmount cashFlow = FastMoney.of(BigDecimal.ZERO, CURRENCY_CODE);
        Integer durationYears = 2;
        BigDecimal apr = new BigDecimal(.08);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new FutureValue(cashFlow, durationYears, apr));
    }

    @Test
    public void testNullCashFlow() {
        Integer durationYears = 2;
        BigDecimal apr = new BigDecimal(.08);

        Assertions.assertThrows(NullPointerException.class, () ->
                new FutureValue(null, durationYears, apr));
    }

    @Test
    public void testInvalidDurationYears() {
        MonetaryAmount cashFlow = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        Integer durationYears = 0;
        BigDecimal apr = new BigDecimal(.08);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new PresentValue(cashFlow, durationYears, apr));
    }

    @Test
    public void testNullDurationYears() {
        MonetaryAmount cashFlow = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        BigDecimal apr = new BigDecimal(.08);

        Assertions.assertThrows(NullPointerException.class, () ->
                new PresentValue(cashFlow, null, apr));
    }

    @Test
    public void testInvalidApr() {
        MonetaryAmount cashFlow = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        Integer durationYears = 2;
        BigDecimal apr = BigDecimal.ZERO;

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new PresentValue(cashFlow, durationYears, apr));
    }

    @Test
    public void testNullApr() {
        MonetaryAmount cashFlow = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        Integer durationYears = 2;

        Assertions.assertThrows(NullPointerException.class, () ->
                new PresentValue(cashFlow, durationYears, null));
    }


    @Test
    public void testValidCalculation() {
        Assertions.assertEquals(new BigDecimal(116.64).setScale(DECIMAL_SCALE, RoundingMode.HALF_UP).stripTrailingZeros().doubleValue(),
                new FutureValue(cashFlow, durationYears, apr).calculate().getNumber().doubleValueExact());
    }

    @Test
    public void testValidCurrencyCode() {
        Assertions.assertEquals(CURRENCY_CODE,
                new FutureValue(cashFlow, durationYears, apr).calculate().getCurrency().getCurrencyCode());
    }

}
