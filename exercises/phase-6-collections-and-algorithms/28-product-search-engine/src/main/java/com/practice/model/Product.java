package com.practice.model;

import java.math.BigDecimal;
import java.util.List;

public class Product {
    private final String productId;
    private final String name;
    private final String category;
    private final BigDecimal priceGBP;
    private final double rating;
    private final boolean inStock;
    private final List<String> tags;

    public Product(String productId, String name, String category, BigDecimal priceGBP, double rating, boolean inStock, List<String> tags) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.priceGBP = priceGBP;
        this.rating = rating;
        this.inStock = inStock;
        this.tags = tags;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public BigDecimal getPriceGBP() { return priceGBP; }
    public double getRating() { return rating; }
    public boolean isInStock() { return inStock; }
    public List<String> getTags() { return tags; }
}
