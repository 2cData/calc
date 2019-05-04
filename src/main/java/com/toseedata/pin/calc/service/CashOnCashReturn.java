package com.toseedata.pin.calc.service;

import com.toseedata.pin.calc.domain.Amount;
import lombok.extern.java.Log;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Log
public final class CashOnCashReturn {

    private static Amount yearOneCashFlow;
    private static Amount yearOneCapitalExpenses;
    private MessageSource messageSource;

    private CashOnCashReturn() {
    }

    //TODO verify the correct error message is thrown like https://www.baeldung.com/guava-preconditions
    public CashOnCashReturn(@NonNull final Amount yearOneCashFlow, @NonNull final Amount yearOneCapitalExpenses) {
        //checkArgument(!yearOneCashFlow.getMoney().isNegativeOrZero(), messageSource.getMessage("cashoncash.CashFlowGreaterThanZero", null, Locale.US));
        checkArgument(!yearOneCashFlow.getMoney().isNegativeOrZero(), "This is wrong");
        checkArgument(!yearOneCapitalExpenses.getMoney().isNegativeOrZero(), "Year One Capital Expenses must be greater than zero");
        checkNotNull(yearOneCashFlow, "Year One Cash Flow must not be null");
        checkNotNull(yearOneCapitalExpenses, "Year One Capital Expenses must not be null");

        CashOnCashReturn.yearOneCashFlow = yearOneCashFlow;
        CashOnCashReturn.yearOneCapitalExpenses = yearOneCapitalExpenses;
    }

    static MonetaryAmount calculate() {
        return yearOneCashFlow.getMoney().divide(yearOneCapitalExpenses.getMoney().getNumber().doubleValueExact());
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
