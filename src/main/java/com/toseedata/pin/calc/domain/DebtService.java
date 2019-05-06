package com.toseedata.pin.calc.domain;

import com.google.common.base.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.javamoney.moneta.FastMoney;

import javax.annotation.Nonnull;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.toseedata.pin.calc.config.Constants.MONTHS_IN_YEAR;

@Data
@NoArgsConstructor
@Log
public final class DebtService {

    private MonetaryAmount principal;
    private BigDecimal apr;
    private int durationMonths;
    private MonetaryAmount payment;
    private CurrencyUnit currencyUnit = Monetary.getCurrency("USD");

    //https://stackoverflow.com/questions/47883931/default-value-in-lombok-how-to-init-default-with-both-constructor-and-builder
    @Builder
    @SuppressWarnings("unused")
    private DebtService(MonetaryAmount principal, BigDecimal apr, int durationMonths, MonetaryAmount payment, CurrencyUnit currencyUnit) {
        this.principal = principal;
        this.apr = apr;
        this.durationMonths = durationMonths;
        this.payment = payment;
        this.currencyUnit = Optional.fromNullable(currencyUnit).or(this.currencyUnit);
    }

    static class calculate {

        /**
         * Number of Periodic Payments (n) = Payments per year times number of years
         * Periodic Interest Rate (i) = Annual rate divided by number of payment periods
         * Discount Factor (D) = {[(1 + i) ^n] - 1} / [i(1 + i)^n]
         * <p>
         * https://www.thebalance.com/loan-payment-calculations-315564
         *
         * @param debtService
         * @return
         */
        public MonetaryAmount paymentsWithPricipalAndInterest(@Nonnull final DebtService debtService) {

            checkArguments(debtService);

            //Periodic Interest Rate (i) = Annual rate divided by number of payment periods
            BigDecimal monthlyInterestRate = calculatePeriodicInterestRate(debtService.apr);

            // Discount Factor (D) = {[(1 + i) ^n] - 1} / [i(1 + i)^n]
            BigDecimal discountFactorNumerator = monthlyInterestRate.multiply(monthlyInterestRate.add(BigDecimal.ONE).pow(debtService.durationMonths));
            BigDecimal discountFactorDenominator = monthlyInterestRate.add(BigDecimal.ONE).pow(debtService.durationMonths).subtract(BigDecimal.ONE);
            BigDecimal discountFactor = discountFactorNumerator.divide(discountFactorDenominator, RoundingMode.HALF_UP);

            // Periodic Payments
            BigDecimal principal = new BigDecimal(debtService.principal.getNumber().doubleValueExact());

            // Payment with interest
            BigDecimal payment = principal.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);

            // payments must be greater than/equal to $1
            assert (payment.compareTo(BigDecimal.ONE) >= 0);

            return FastMoney.of(payment, debtService.currencyUnit);
        }

        /**
         * Interest Only Payment = loan balance x (annual interest rate/12) Interest Only Payment
         *
         * @param debtService
         * @return
         */
        public MonetaryAmount paymentsWithInterestOnly(@Nonnull final DebtService debtService) {

            checkArguments(debtService);

            //Periodic Interest Rate (i) = Annual rate divided by number of payment periods
            BigDecimal monthlyInterestRate = calculatePeriodicInterestRate(debtService.apr);

            // Periodic Payments
            BigDecimal payment = new BigDecimal(debtService.principal.getNumber().doubleValueExact()).multiply(monthlyInterestRate);

            // payments must be greater than/equal to $1
            assert (payment.compareTo(BigDecimal.ONE) >= 0);

            return FastMoney.of(payment, debtService.currencyUnit);
        }

        /**
         * There are a common set of arguments
         *
         * @param debtService
         */
        private void checkArguments(final DebtService debtService) {
            checkArgument(!debtService.principal.isNegativeOrZero(), "Principal must be greater than zero");
            checkArgument(debtService.apr.compareTo(BigDecimal.ZERO) > 0, "Apr must be greater than zero");
            checkArgument(debtService.durationMonths > 0, "Duration must be greater than zero");

            checkNotNull(debtService.principal, "Principal must not be null");
            checkNotNull(debtService.apr, "APR must not be null");
        }

        /**
         * Periodic Interest Rate (i) = Annual rate divided by number of payment periods
         * Make sure the rounding is consistent
         *
         * @return
         */
        private BigDecimal calculatePeriodicInterestRate(final BigDecimal apr) {
            return apr.divide(MONTHS_IN_YEAR, RoundingMode.HALF_UP);
        }
    }
}
