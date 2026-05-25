package com.practice.model;

import java.math.BigDecimal;

public class ProductSummary {
    private final String productName;
    private final int totalQuantity;
    private final BigDecimal totalRevenueGBP;

    public ProductSummary(String productName, int totalQuantity, BigDecimal totalRevenueGBP) {
        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.totalRevenueGBP = totalRevenueGBP;
    }

    public String getProductName() { return productName; }
    public int getTotalQuantity() { return totalQuantity; }
    public BigDecimal getTotalRevenueGBP() { return totalRevenueGBP; }
}
