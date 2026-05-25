package com.practice.model;

import java.math.BigDecimal;

public class TaxBandResult {
    private final String bandName;
    private final int ratePercent;
    private final BigDecimal incomeInBandGBP;
    private final BigDecimal taxInBandGBP;

    public TaxBandResult(String bandName, int ratePercent, BigDecimal incomeInBandGBP, BigDecimal taxInBandGBP) {
        this.bandName = bandName;
        this.ratePercent = ratePercent;
        this.incomeInBandGBP = incomeInBandGBP;
        this.taxInBandGBP = taxInBandGBP;
    }

    public String getBandName() { return bandName; }
    public int getRatePercent() { return ratePercent; }
    public BigDecimal getIncomeInBandGBP() { return incomeInBandGBP; }
    public BigDecimal getTaxInBandGBP() { return taxInBandGBP; }
}
