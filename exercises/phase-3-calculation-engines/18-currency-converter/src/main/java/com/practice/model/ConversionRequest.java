package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ConversionRequest {
    private final BigDecimal amount;
    private final String fromCurrency;
    private final String toCurrency;
    private final LocalDate asOfDate;

    public ConversionRequest(BigDecimal amount, String fromCurrency, String toCurrency, LocalDate asOfDate) {
        this.amount = amount;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.asOfDate = asOfDate;
    }

    public BigDecimal getAmount() { return amount; }
    public String getFromCurrency() { return fromCurrency; }
    public String getToCurrency() { return toCurrency; }
    public LocalDate getAsOfDate() { return asOfDate; }
}
