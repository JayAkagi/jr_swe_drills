package com.practice.model;

import java.math.BigDecimal;

public class OrderItem {

    private final String productId;
    private final int quantity;
    private final BigDecimal unitPriceGBP;

    public OrderItem(String productId, int quantity, BigDecimal unitPriceGBP) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPriceGBP = unitPriceGBP;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPriceGBP() {
        return unitPriceGBP;
    }
}
