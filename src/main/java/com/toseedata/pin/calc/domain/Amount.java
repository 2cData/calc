package com.toseedata.pin.calc.domain;

import lombok.Value;
import org.javamoney.moneta.FastMoney;

import javax.annotation.Nonnull;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.UnknownCurrencyException;
import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Value
public class Amount {

    private javax.money.CurrencyUnit currencyUnit;
    private MonetaryAmount money;

    public Amount(@Nonnull final BigDecimal amount, String currencyCode) {
        checkNotNull(amount, "Amount is not optional and must not be null");
        checkNotNull(currencyCode, "Currency code must not be null but can be USD by default");

        try {
            this.currencyUnit = Monetary.getCurrency(currencyCode);
        } catch (UnknownCurrencyException ex) {
            throw new IllegalArgumentException("Currency code not found: " + ex.getCurrencyCode());
        }
        this.money = FastMoney.of(amount, currencyCode);
    }

    public Amount(@Nonnull final BigDecimal amount) {
        checkNotNull(amount, "Amount is not optional and must not be null");

        this.currencyUnit = Monetary.getCurrency("USD");
        this.money = FastMoney.of(amount, "USD");
    }


    public Amount(@Nonnull final List<BigDecimal> amounts, String currencyCode) {
        checkNotNull(amounts, "Amount is not optional and must not be null");
        checkNotNull(currencyCode, "Currency code must not be null but can be USD by default");

        try {
            this.currencyUnit = Monetary.getCurrency(currencyCode);
        } catch (UnknownCurrencyException ex) {
            throw new IllegalArgumentException("Currency code not found: " + ex.getCurrencyCode());
        }
        this.money = FastMoney.of(amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add), currencyCode);
    }


    public Amount(@Nonnull final List<BigDecimal> amounts) {
        checkNotNull(amounts, "Amount is not optional and must not be null");

        this.money = FastMoney.of(amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add), "USD");
        this.currencyUnit = Monetary.getCurrency("USD");
    }
}
