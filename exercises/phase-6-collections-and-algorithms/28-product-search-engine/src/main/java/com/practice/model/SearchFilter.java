package com.practice.model;

import java.math.BigDecimal;
import java.util.List;

public class SearchFilter {
    private final String category;
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;
    private final Double minRating;
    private final boolean inStockOnly;
    private final List<String> tags;

    public SearchFilter(String category, BigDecimal minPrice, BigDecimal maxPrice, Double minRating, boolean inStockOnly, List<String> tags) {
        this.category = category;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minRating = minRating;
        this.inStockOnly = inStockOnly;
        this.tags = tags;
    }

    public String getCategory() { return category; }
    public BigDecimal getMinPrice() { return minPrice; }
    public BigDecimal getMaxPrice() { return maxPrice; }
    public Double getMinRating() { return minRating; }
    public boolean isInStockOnly() { return inStockOnly; }
    public List<String> getTags() { return tags; }
}
