package com.practice.model;

import java.math.BigDecimal;

public class InventoryItem {
    private final String sku;
    private final String productName;
    private final int stockLevel;
    private final BigDecimal unitPriceGBP;

    public InventoryItem(String sku, String productName, int stockLevel, BigDecimal unitPriceGBP) {
        this.sku = sku;
        this.productName = productName;
        this.stockLevel = stockLevel;
        this.unitPriceGBP = unitPriceGBP;
    }

    public String getSku() { return sku; }
    public String getProductName() { return productName; }
    public int getStockLevel() { return stockLevel; }
    public BigDecimal getUnitPriceGBP() { return unitPriceGBP; }
}
