package com.practice.model;

import java.math.BigDecimal;

public class TaxInput {
    private final BigDecimal annualIncomeGBP;
    private final int taxYear;
    private final BigDecimal pensionContributionGBP;
    private final boolean marriageAllowanceTransferred;

    public TaxInput(BigDecimal annualIncomeGBP, int taxYear, BigDecimal pensionContributionGBP, boolean marriageAllowanceTransferred) {
        this.annualIncomeGBP = annualIncomeGBP;
        this.taxYear = taxYear;
        this.pensionContributionGBP = pensionContributionGBP;
        this.marriageAllowanceTransferred = marriageAllowanceTransferred;
    }

    public BigDecimal getAnnualIncomeGBP() { return annualIncomeGBP; }
    public int getTaxYear() { return taxYear; }
    public BigDecimal getPensionContributionGBP() { return pensionContributionGBP; }
    public boolean isMarriageAllowanceTransferred() { return marriageAllowanceTransferred; }
}
