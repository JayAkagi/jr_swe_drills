package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ConversionResult {
    private final BigDecimal originalAmount;
    private final String fromCurrency;
    private final BigDecimal convertedAmount;
    private final String toCurrency;
    private final BigDecimal rateUsed;
    private final LocalDate rateDate;
    private final String warning;

    public ConversionResult(BigDecimal originalAmount, String fromCurrency, BigDecimal convertedAmount,
                            String toCurrency, BigDecimal rateUsed, LocalDate rateDate, String warning) {
        this.originalAmount = originalAmount;
        this.fromCurrency = fromCurrency;
        this.convertedAmount = convertedAmount;
        this.toCurrency = toCurrency;
        this.rateUsed = rateUsed;
        this.rateDate = rateDate;
        this.warning = warning;
    }

    public BigDecimal getOriginalAmount() { return originalAmount; }
    public String getFromCurrency() { return fromCurrency; }
    public BigDecimal getConvertedAmount() { return convertedAmount; }
    public String getToCurrency() { return toCurrency; }
    public BigDecimal getRateUsed() { return rateUsed; }
    public LocalDate getRateDate() { return rateDate; }
    public String getWarning() { return warning; }
}
