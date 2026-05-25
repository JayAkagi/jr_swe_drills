package com.practice.model;

import java.math.BigDecimal;

public class DiscountLine {
    private final String description;
    private final BigDecimal amountGBP;

    public DiscountLine(String description, BigDecimal amountGBP) {
        this.description = description;
        this.amountGBP = amountGBP;
    }

    public String getDescription() { return description; }
    public BigDecimal getAmountGBP() { return amountGBP; }
}
