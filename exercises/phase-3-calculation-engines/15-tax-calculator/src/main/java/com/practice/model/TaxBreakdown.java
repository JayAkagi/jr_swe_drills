package com.practice.model;

import java.math.BigDecimal;
import java.util.List;

public class TaxBreakdown {
    private final BigDecimal personalAllowance;
    private final BigDecimal taxableIncome;
    private final List<TaxBandResult> bands;
    private final BigDecimal totalIncomeTaxGBP;
    private final BigDecimal totalNIGBP;
    private final BigDecimal totalDeductionsGBP;
    private final BigDecimal takeHomeGBP;

    public TaxBreakdown(BigDecimal personalAllowance, BigDecimal taxableIncome, List<TaxBandResult> bands,
                        BigDecimal totalIncomeTaxGBP, BigDecimal totalNIGBP,
                        BigDecimal totalDeductionsGBP, BigDecimal takeHomeGBP) {
        this.personalAllowance = personalAllowance;
        this.taxableIncome = taxableIncome;
        this.bands = bands;
        this.totalIncomeTaxGBP = totalIncomeTaxGBP;
        this.totalNIGBP = totalNIGBP;
        this.totalDeductionsGBP = totalDeductionsGBP;
        this.takeHomeGBP = takeHomeGBP;
    }

    public BigDecimal getPersonalAllowance() { return personalAllowance; }
    public BigDecimal getTaxableIncome() { return taxableIncome; }
    public List<TaxBandResult> getBands() { return bands; }
    public BigDecimal getTotalIncomeTaxGBP() { return totalIncomeTaxGBP; }
    public BigDecimal getTotalNIGBP() { return totalNIGBP; }
    public BigDecimal getTotalDeductionsGBP() { return totalDeductionsGBP; }
    public BigDecimal getTakeHomeGBP() { return takeHomeGBP; }
}
