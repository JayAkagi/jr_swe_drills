package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanDetails {
    private final BigDecimal principalGBP;
    private final BigDecimal annualInterestRatePercent;
    private final int termMonths;
    private final LocalDate startDate;

    public LoanDetails(BigDecimal principalGBP, BigDecimal annualInterestRatePercent, int termMonths, LocalDate startDate) {
        this.principalGBP = principalGBP;
        this.annualInterestRatePercent = annualInterestRatePercent;
        this.termMonths = termMonths;
        this.startDate = startDate;
    }

    public BigDecimal getPrincipalGBP() { return principalGBP; }
    public BigDecimal getAnnualInterestRatePercent() { return annualInterestRatePercent; }
    public int getTermMonths() { return termMonths; }
    public LocalDate getStartDate() { return startDate; }
}
