package com.toseedata.pin.calc.domain;

import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.FastMoney;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

import static com.toseedata.pin.calc.config.Constants.CURRENCY_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class CapRateTest {

    @Autowired
    CapRate capRate;

    long fairMarketValue = 100;
    MonetaryAmount fairMarketValueAmount = FastMoney.of(fairMarketValue, CURRENCY_CODE);

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void testInvalidNOI() {
        MonetaryAmount noi = FastMoney.of(BigDecimal.ZERO, CURRENCY_CODE);
        MonetaryAmount fmv = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CapRate(noi, fmv));
    }

    @Test
    void testNullNOI() {
        MonetaryAmount fmv = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);

        Assertions.assertThrows(NullPointerException.class, () ->
                new CapRate(null, fmv));
    }

    @Test
    void testInvalidFMV() {
        MonetaryAmount noi = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        MonetaryAmount fmv = FastMoney.of(BigDecimal.ZERO, CURRENCY_CODE);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CapRate(noi, fmv));
    }

    @Test
    void testNullFMV() {
        MonetaryAmount noi = FastMoney.of(BigDecimal.ONE, CURRENCY_CODE);
        Assertions.assertThrows(NullPointerException.class, () ->
                new CapRate(noi, null));
    }

    @Test
    void calculateCapRate() {
        // Given
        // a $100 Fair Market Value

        // When
        // NOI are 1/10th the FMV
        MonetaryAmount noi = fairMarketValueAmount.divide(10);

        // Then
        // cash on cash return is 100 / 10 = 10
        capRate = new CapRate(noi, fairMarketValueAmount);

        assertEquals(
                CapRate.calculate().doubleValue(), 0.1);

    }

}
