package com.practice.model;

import java.math.BigDecimal;

public class BenefitDeduction {
    private final String name;
    private final BigDecimal monthlyAmountGBP;

    public BenefitDeduction(String name, BigDecimal monthlyAmountGBP) {
        this.name = name;
        this.monthlyAmountGBP = monthlyAmountGBP;
    }

    public String getName() { return name; }
    public BigDecimal getMonthlyAmountGBP() { return monthlyAmountGBP; }
}
