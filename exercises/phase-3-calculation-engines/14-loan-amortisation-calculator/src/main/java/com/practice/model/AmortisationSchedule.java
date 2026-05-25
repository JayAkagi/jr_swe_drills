package com.practice.model;

import java.math.BigDecimal;
import java.util.List;

public class AmortisationSchedule {
    private final List<RepaymentRow> rows;
    private final BigDecimal monthlyPaymentGBP;
    private final BigDecimal totalInterestGBP;
    private final BigDecimal totalPaidGBP;

    public AmortisationSchedule(List<RepaymentRow> rows, BigDecimal monthlyPaymentGBP,
                                BigDecimal totalInterestGBP, BigDecimal totalPaidGBP) {
        this.rows = rows;
        this.monthlyPaymentGBP = monthlyPaymentGBP;
        this.totalInterestGBP = totalInterestGBP;
        this.totalPaidGBP = totalPaidGBP;
    }

    public List<RepaymentRow> getRows() { return rows; }
    public BigDecimal getMonthlyPaymentGBP() { return monthlyPaymentGBP; }
    public BigDecimal getTotalInterestGBP() { return totalInterestGBP; }
    public BigDecimal getTotalPaidGBP() { return totalPaidGBP; }
}
