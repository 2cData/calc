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
 * Use of the Present Value Factor Formula
 * By calculating the current value today per dollar received at a future date,
 * the formula for the present value factor could then be used to calculate an amount larger than a dollar.
 * This can be done by multiplying the present value factor by the amount received at a future date.
 *
 * For example, if an individual is wanting to use the present value factor to calculate today's value of $500 received in 3 years based on a 10% rate, then the individual could multiply $500 times the present value factor of 3 years and 10%.
 *
 * The present value factor is usually found on a table that lists the factors based on the term (n) and the rate (r).
 * Once the present value factor is found based on the term and rate, it can be multiplied by the dollar amount to find the present value.
 * Using the formula on the prior example, the present value factor of 3 years and 10% is .751, so $500 times .751 equals $375.66.
 *
 * PV Factor = 1 / (1+r)^n
 *
 * http://financeformulas.net/Present_Value_Factor.html
 */
public class PresentValueFactorTest {

    @Autowired
    private PresentValueFactor presentValueFactor;

    private final static Integer numberofPeriods = 3;
    private final static BigDecimal rate = new BigDecimal(.1);
    private final static BigDecimal futureValue = new BigDecimal(500);
    private final static BigDecimal presentValue = new BigDecimal(375.65).setScale(2,RoundingMode.HALF_UP);
    private final static BigDecimal presentValueFactorResult = new BigDecimal(.7513).setScale(DECIMAL_SCALE,RoundingMode.HALF_UP);

    @Test
    public void testInvalidNumberOfPeriods() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new PresentValueFactor(0, rate));
    }

    @Test
    public void testNullNumberOfPeriods() {

        Assertions.assertThrows(NullPointerException.class, () ->
                new PresentValueFactor(null, rate));
    }

    @Test
    public void testInvalidRate() {

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new PresentValueFactor(numberofPeriods, BigDecimal.ZERO));
    }

    @Test
    public void testNullRate() {

        Assertions.assertThrows(NullPointerException.class, () ->
                new PresentValueFactor(numberofPeriods, null));
    }

    @Test
    public void testSamplePresentValueFactor() {

        Assertions.assertEquals(presentValueFactorResult,
                 new PresentValueFactor(numberofPeriods, rate).calculate());
    }

    @Test
    public void testSamplePresentValueFactorCalculation() {
        Assertions.assertEquals(presentValue,
                futureValue.multiply(presentValueFactorResult).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());

    }
}
