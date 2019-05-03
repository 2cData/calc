package com.toseedata.pin.calc;

import java.math.BigDecimal;

public class CashOnCashReturn {

    public CashOnCashReturn(){}

    public double calculateCashOnCashReturn(double yearOneCashFlow, double yearOneCapitalExpenses) {
        return yearOneCashFlow / yearOneCapitalExpenses;
    }
}
