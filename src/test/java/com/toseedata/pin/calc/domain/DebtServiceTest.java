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
 * Regular Loan
 * Number of Periodic Payments (n) = Payments per year times number of years
 * Periodic Interest Rate (i) = Annual rate divided by number of payment periods
 * Discount Factor (D) = {[(1 + i) ^n] - 1} / [i(1 + i)^n]
 * <p>
 * https://www.thebalance.com/loan-payment-calculations-315564
 *
 * Interest Only
 * Interest Only Payment = loan balance x (annual interest rate/12) Interest Only Payment
 *
 * Example
 * Loan Principal (P): $200,000
 * APR: 4%
 * Duration: 30 years
 */
public class DebtServiceTest {

    @Autowired
    private DebtService debtService;


    MonetaryAmount principal = FastMoney.of(200000, "USD");
    BigDecimal apr = new BigDecimal(.04);
    int durationMonths = 30 * 12;
    MonetaryAmount paymentPI = FastMoney.of(954.83060, "USD");
    MonetaryAmount paymentI = FastMoney.of(666.66670, "USD");


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void testPaymentsWithPrincipalAndInterestInvalidPrincipal() {
        DebtService debtService = DebtService.builder()
                .principal(FastMoney.of(0, CURRENCY_CODE))
                .apr(apr)
                .durationMonths(durationMonths)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithPrincipalAndInterestNullPrincipal() {
        DebtService debtService = DebtService.builder()
                .apr(apr)
                .durationMonths(durationMonths)
                .build();

        Assertions.assertThrows(NullPointerException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithPrincipalAndInterestInvalidAPR() {
        DebtService debtService = DebtService.builder()
                .principal(FastMoney.of(1, CURRENCY_CODE))
                .apr(BigDecimal.ZERO)
                .durationMonths(durationMonths)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithPrincipalAndInterestNullAPR() {
        DebtService debtService = DebtService.builder()
                .principal(FastMoney.of(1, CURRENCY_CODE))
                .durationMonths(durationMonths)
                .build();

        Assertions.assertThrows(NullPointerException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }


    @Test
    void testPaymentsWithPrincipalAndInterestInvalidDurationMonths() {
        DebtService debtService = DebtService.builder()
                .principal(principal)
                .apr(apr)
                .durationMonths(-1)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithPrincipalAndInterestEmptyDurationMonths() {
        DebtService debtService = DebtService.builder()
                .principal(FastMoney.of(1, CURRENCY_CODE))
                .apr(apr)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithInterestOnlyInvalidPrincipal() {
        DebtService debtService = DebtService.builder()
                .principal(FastMoney.of(0, CURRENCY_CODE))
                .apr(apr)
                .durationMonths(durationMonths)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithInterestOnlyNullPrincipal() {
        DebtService debtService = DebtService.builder()
                .apr(apr)
                .durationMonths(durationMonths)
                .build();

        Assertions.assertThrows(NullPointerException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithInterestOnlyInvalidAPR() {
        DebtService debtService = DebtService.builder()
                .principal(FastMoney.of(1, CURRENCY_CODE))
                .apr(BigDecimal.ZERO)
                .durationMonths(durationMonths)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithInterestOnlyNullAPR() {
        DebtService debtService = DebtService.builder()
                .principal(FastMoney.of(1, CURRENCY_CODE))
                .durationMonths(durationMonths)
                .build();

        Assertions.assertThrows(NullPointerException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithInterestOnlyInvalidDurationMonths() {
        DebtService debtService = DebtService.builder()
                .principal(principal)
                .apr(apr)
                .durationMonths(-1)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithInterestOnlyEmptyDurationMonths() {
        DebtService debtService = DebtService.builder()
                .principal(FastMoney.of(1, CURRENCY_CODE))
                .apr(apr)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new DebtService.calculate().paymentsWithInterestOnly(debtService));
    }

    @Test
    public void testCalculateMonthlyPaymentsForPrincipalInterest() {
        // Given
        // A 200,000 loan for 30 years at 4%
        DebtService debtService = DebtService.builder()
                .principal(principal)
                .apr(apr)
                .durationMonths(durationMonths)
                .build();

        // When
        // calculating the monthly expense paying principal and interest
        MonetaryAmount payment = new DebtService.calculate().paymentsWithPricipalAndInterest(debtService);

        // Then
        // the payment should equal
        assertEquals(
                payment, this.paymentPI);
    }

    @Test
    public void testCalculateMonthlyPaymentsForInterestOnly() {
        // Given
        // A 200,000 loan for 30 years at 4%
        DebtService debtService = DebtService.builder()
                .principal(principal)
                .apr(apr)
                .durationMonths(durationMonths)
                .build();

        // When
        // calculating the monthly expense paying interest only
        MonetaryAmount payment = new DebtService.calculate().paymentsWithInterestOnly(debtService);

        // Then
        // the payment should equal
        assertEquals(
                payment, this.paymentI);
    }

}
