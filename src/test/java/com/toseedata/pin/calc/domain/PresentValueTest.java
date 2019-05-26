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
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Let's assume we are to receive $100 at the end of two years.
 * How do we calculate the present value of the amount, assuming the interest rate is 8% per year compounded annually?
 * PV = 85.73
 * PV = FV(1 + i)^-n or PV = FV[1/(1+i)n]
 *
 */
public class PresentValueTest {

    @Autowired
    private PresentValue presentValue;

    long fairMarketValue = 100;
    MonetaryAmount futureValue = FastMoney.of(fairMarketValue, CURRENCY_CODE);
    Integer durationYears = 2;
    BigDecimal apr = new BigDecimal(.08);

    @Test
    public void testInvalidFutureValue() {

        MonetaryAmount futureValue = FastMoney.of(BigDecimal.ZERO, CURRENCY_CODE);
        Integer durationYears = 2;
        BigDecimal apr = new BigDecimal(.08);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new PresentValue(futureValue,durationYears,apr));
    }

    @Test
    public void testNullFutureValue() {
        Integer durationYears = 2;
        BigDecimal apr = new BigDecimal(.08);

        Assertions.assertThrows(NullPointerException.class, () ->
                new PresentValue(null,durationYears,apr));
    }

    @Test
    public void testInvalidDurationYears() {
        MonetaryAmount futureValue = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        Integer durationYears = 0;
        BigDecimal apr = new BigDecimal(.08);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new PresentValue(futureValue,durationYears,apr));
    }

    @Test
    public void testNullDurationYears() {
        MonetaryAmount futureValue = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        BigDecimal apr = new BigDecimal(.08);

        Assertions.assertThrows(NullPointerException.class, () ->
                new PresentValue(futureValue,null,apr));
    }

    @Test
    public void testInvalidApr() {
        MonetaryAmount futureValue = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        Integer durationYears = 2;
        BigDecimal apr = BigDecimal.ZERO;

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new PresentValue(futureValue,durationYears,apr));
    }

    @Test
    public void testNullApr() {
        MonetaryAmount futureValue = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        Integer durationYears = 2;

        Assertions.assertThrows(NullPointerException.class, () ->
                new PresentValue(futureValue,durationYears,null));
    }


    @Test
    public void testValidCalculation() {
        Assertions.assertEquals(new BigDecimal(85.73).setScale(DECIMAL_SCALE,RoundingMode.HALF_UP).stripTrailingZeros().doubleValue(),
                new PresentValue(futureValue,durationYears,apr).calculate().getNumber().doubleValueExact());
    }
}
