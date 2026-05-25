package com.practice.model;

import java.math.BigDecimal;

public class CartItem {
    private final String productId;
    private final String name;
    private final ProductCategory category;
    private final BigDecimal unitPriceGBP;
    private final int quantity;

    public CartItem(String productId, String name, ProductCategory category, BigDecimal unitPriceGBP, int quantity) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.unitPriceGBP = unitPriceGBP;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public ProductCategory getCategory() { return category; }
    public BigDecimal getUnitPriceGBP() { return unitPriceGBP; }
    public int getQuantity() { return quantity; }
}
