package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExchangeRate {
    private final String fromCurrency;
    private final String toCurrency;
    private final BigDecimal rate;
    private final LocalDate validFrom;
    private final LocalDate validUntil;

    public ExchangeRate(String fromCurrency, String toCurrency, BigDecimal rate, LocalDate validFrom, LocalDate validUntil) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public String getFromCurrency() { return fromCurrency; }
    public String getToCurrency() { return toCurrency; }
    public BigDecimal getRate() { return rate; }
    public LocalDate getValidFrom() { return validFrom; }
    public LocalDate getValidUntil() { return validUntil; }
}
