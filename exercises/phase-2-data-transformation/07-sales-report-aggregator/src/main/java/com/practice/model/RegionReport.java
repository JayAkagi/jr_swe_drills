package com.practice.model;

import java.math.BigDecimal;
import java.util.List;

public class RegionReport {
    private final String region;
    private final List<ProductSummary> products;
    private final BigDecimal totalRevenueGBP;

    public RegionReport(String region, List<ProductSummary> products, BigDecimal totalRevenueGBP) {
        this.region = region;
        this.products = products;
        this.totalRevenueGBP = totalRevenueGBP;
    }

    public String getRegion() { return region; }
    public List<ProductSummary> getProducts() { return products; }
    public BigDecimal getTotalRevenueGBP() { return totalRevenueGBP; }
}
