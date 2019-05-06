package com.toseedata.pin.calc.domain;

import lombok.extern.java.Log;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Log
public final class CashOnCashReturn {

    private static MonetaryAmount yearOneCashFlow;
    private static MonetaryAmount yearOneCapitalExpenses;

    /**
     *
     * @param yearOneCashFlow
     * @param yearOneCapitalExpenses
     */
    public CashOnCashReturn(@NonNull final MonetaryAmount yearOneCashFlow, @NonNull final MonetaryAmount yearOneCapitalExpenses) {
        checkArgument(!yearOneCashFlow.isNegativeOrZero(), "Year One Cash Flow must be greater than zero");
        checkArgument(!yearOneCapitalExpenses.isNegativeOrZero(), "Year One Capital Expenses must be greater than zero");
        checkNotNull(yearOneCashFlow, "Year One Cash Flow must not be null");
        checkNotNull(yearOneCapitalExpenses, "Year One Capital Expenses must not be null");

        CashOnCashReturn.yearOneCashFlow = yearOneCashFlow;
        CashOnCashReturn.yearOneCapitalExpenses = yearOneCapitalExpenses;
    }

    static MonetaryAmount calculate() {
        MonetaryAmount result =
                yearOneCashFlow.divide(
                        yearOneCapitalExpenses.getNumber().doubleValueExact());

        assert (!result.isZero());

        return result;
    }

}
