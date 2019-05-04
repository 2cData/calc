package com.toseedata.pin.calc.domain;

import org.javamoney.moneta.FastMoney;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmountTest {

    @Test
    public void testAmountWithValidAmountAndValidCurrencyCode() {
        BigDecimal amount = new BigDecimal(BigInteger.ONE);
        String currencyCode = "USD";
        Amount result = new Amount(amount, currencyCode);

        assertEquals(result.getMoney(), FastMoney.of(amount, currencyCode));
        assertEquals(result.getCurrencyUnit().getCurrencyCode(), currencyCode);
    }

    @Test
    public void testAmountWithNullAmountAndValidCurrencyCode() {
        BigDecimal amount = null;
        String currencyCode = "USD";

        Assertions.assertThrows(NullPointerException.class, () ->
                new Amount(amount, currencyCode));
    }

    @Test
    public void testAmountWithValidAmountAndNullCurrencyCode() {
        BigDecimal amount = new BigDecimal(BigInteger.ONE);
        String currencyCode = null;

        Assertions.assertThrows(NullPointerException.class, () ->
                new Amount(amount, currencyCode));
    }

    @Test
    public void testAmountWithValidAmountAndInvalidCurrencyCode() {
        BigDecimal amount = new BigDecimal(BigInteger.ONE);
        String currencyCode = "XYZ";

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Amount(amount, currencyCode));
    }

}