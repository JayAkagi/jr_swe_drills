package com.practice.model;

import java.math.BigDecimal;
import java.util.List;

public class CartTotal {
    private final BigDecimal subtotalGBP;
    private final List<DiscountLine> discountBreakdown;
    private final BigDecimal totalDiscountGBP;
    private final BigDecimal finalTotalGBP;

    public CartTotal(BigDecimal subtotalGBP, List<DiscountLine> discountBreakdown, BigDecimal totalDiscountGBP, BigDecimal finalTotalGBP) {
        this.subtotalGBP = subtotalGBP;
        this.discountBreakdown = discountBreakdown;
        this.totalDiscountGBP = totalDiscountGBP;
        this.finalTotalGBP = finalTotalGBP;
    }

    public BigDecimal getSubtotalGBP() { return subtotalGBP; }
    public List<DiscountLine> getDiscountBreakdown() { return discountBreakdown; }
    public BigDecimal getTotalDiscountGBP() { return totalDiscountGBP; }
    public BigDecimal getFinalTotalGBP() { return finalTotalGBP; }
}
