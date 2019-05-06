package com.toseedata.pin.calc.domain;

import org.javamoney.moneta.FastMoney;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
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
                .principal(FastMoney.of(0, "USD"))
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
                .principal(FastMoney.of(1, "USD"))
                .apr(BigDecimal.ZERO)
                .durationMonths(durationMonths)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithPrincipalAndInterestNullAPR() {
        DebtService debtService = DebtService.builder()
                .principal(FastMoney.of(1, "USD"))
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
                .principal(FastMoney.of(1, "USD"))
                .apr(apr)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithInterestOnlyInvalidPrincipal() {
        DebtService debtService = DebtService.builder()
                .principal(FastMoney.of(0, "USD"))
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
                .principal(FastMoney.of(1, "USD"))
                .apr(BigDecimal.ZERO)
                .durationMonths(durationMonths)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new DebtService.calculate().paymentsWithPricipalAndInterest(debtService));
    }

    @Test
    void testPaymentsWithInterestOnlyNullAPR() {
        DebtService debtService = DebtService.builder()
                .principal(FastMoney.of(1, "USD"))
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
                .principal(FastMoney.of(1, "USD"))
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



    /*
    @Test
    public void testCalculateMonthlyPaymentsKnowingPrincipalInterestAndDuration() {
        // Given
        // A 200,000 loan for 30 years at 4%

        // When
        // calculating the monthly expense

        // Then
        // the result is 954.83
        //MonetaryAmount result = FastMoney.of(payment, "USD");
        //calculate(principal, apr, duration)
        //MortgagePayment a = MortgagePayment.builder().apr(.04).duration(30).principal(200000).build();
        //System.out.println(new MortgagePayment.calculate().calculatePayment(a));

        double monthlyInterestRate = .04/12;
        //System.out.println("monthlyInterestRate " + monthlyInterestRate);

        double numberOfMonths = 30 * 12;
        //System.out.println("numberOfMonths " + numberOfMonths);

        double a = monthlyInterestRate * (1 + monthlyInterestRate);
        //System.out.println(a);

        double b = Math.pow(a, 360);
        //System.out.println(b);


        BigDecimal monthlyInterestRate1 = new BigDecimal(.04/12);
        //System.out.println("monthlyInterestRate1 " + monthlyInterestRate1);

        BigDecimal c = monthlyInterestRate1.multiply(monthlyInterestRate1.add(BigDecimal.ONE));
        //System.out.println("multiply c :" + c);

        BigDecimal d = c.pow(360);
        //System.out.println("raise c to the owe of 360: " + d);

        //PEDMAS
        BigDecimal P = monthlyInterestRate1.add(BigDecimal.ONE);
        System.out.println("P: " + P);

        BigDecimal E = P.pow(360);
        System.out.println("E: " + E);

        BigDecimal M = monthlyInterestRate1.multiply(E);
        System.out.println("M: " + M);

        BigDecimal numerator = M;
        BigDecimal denominator = P.pow(360).subtract(BigDecimal.ONE);
        System.out.println("numerator: " + numerator);
        System.out.println("denominator: " + denominator);
        BigDecimal result = numerator.divide(denominator,20, RoundingMode.UP);
        System.out.println("result: " + result);
        BigDecimal loanAmount = new BigDecimal(200000);
        BigDecimal payment = result.multiply(loanAmount);
        System.out.println("payment: " + payment);

        //double numerator = Math.pow(monthlyInterestRate*(1 + monthlyInterestRate),numberOfMonths);
        //System.out.println(numerator);
        //BigDecimal monthlyInterestRate = mortgagePayment.apr.divide(12);
        //int numberOfMonths = mortgagePayment.duration * 12;

        //BigDecimal numerator = new BigDecimal(monthlyInterestRate.multiply(monthlyInterestRate.add(1).pow(numberOfMonths)));

        // Given
        // A 200,000 loan for 30 years at 4%

        // When
        // calculating the monthly expense

        // Then
        // the result is 954.83
        MonetaryAmount result1 = FastMoney.of(10, "USD");

        //MortgagePayment a = MortgagePayment.builder().currencyUnit("USD").build();
        //calculate(principal, apr, duration)
    }

     */


    /**
     * paymentsWithPricipalAndInterest(@Nonnull final DebtService debtService) {
     * checkArgument(!debtService.principal.isNegativeOrZero(), "Principal must be greater than zero");
     * checkArgument(debtService.apr.compareTo(BigDecimal.ZERO) > 0, "Apr must be greater than zero");
     * checkArgument(debtService.durationMonths > 0, "Duration must be greater than zero");
     * <p>
     * checkNotNull(debtService.principal, "Principal must not be null");
     * checkNotNull(debtService.apr, "APR must not be null");
     * checkNotNull(debtService.durationMonths, "Duration must not be null");
     */


    @Test
    public void testCalculatePrincipalKnowingMonthlyPaymentsAndInterestAndDuration() {
        // Given
        // A 200,000 loan for 30 years at 4%

        // When
        // calculating the monthly expense

        // Then
        // the result is 954.83

        MonetaryAmount result = FastMoney.of(10, "USD");
        //DebtService mortgagePayment = DebtService.builder().principal(new MonetaryAmount(new BigDecimal(200000))).apr(new BigDecimal(.04)).durationMonths(360).build();

        //MortgagePayment mortgagePayment = MortgagePayment.builder().apr(.04).duration(30).principal(200000).build();
        //System.out.println(new MortgagePayment.calculate().calculatePayment(
        //calculate(principal, apr, duration);

        //MonetaryAmount payment = new MortgagePayment.calculate().calculatePayment(mortgagePayment);
        //System.out.println("--------------");
        //System.out.println(payment.getNumber());
        //System.out.println(payment.getCurrency());
        //System.out.println("--------------");
        //BigDecimal payment =  new MortgagePayment.calculate().calculatePayment(mortgagePayment);
        //MonetaryAmount payment  =  new DebtService().calculate();
        System.out.println("--------------");
        //System.out.println(payment.getMoney());
        //System.out.println(payment.getCurrencyUnit());
        System.out.println("--------------");

    }

}
