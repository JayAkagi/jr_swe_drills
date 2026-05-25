package com.practice.model;

import java.math.BigDecimal;

public class StockChange {
    private final String sku;
    private final String productName;
    private final int previousStock;
    private final int newStock;
    private final int stockDelta;
    private final BigDecimal previousPrice;
    private final BigDecimal newPrice;
    private final boolean priceChanged;

    public StockChange(String sku, String productName, int previousStock, int newStock,
                       int stockDelta, BigDecimal previousPrice, BigDecimal newPrice, boolean priceChanged) {
        this.sku = sku;
        this.productName = productName;
        this.previousStock = previousStock;
        this.newStock = newStock;
        this.stockDelta = stockDelta;
        this.previousPrice = previousPrice;
        this.newPrice = newPrice;
        this.priceChanged = priceChanged;
    }

    public String getSku() { return sku; }
    public String getProductName() { return productName; }
    public int getPreviousStock() { return previousStock; }
    public int getNewStock() { return newStock; }
    public int getStockDelta() { return stockDelta; }
    public BigDecimal getPreviousPrice() { return previousPrice; }
    public BigDecimal getNewPrice() { return newPrice; }
    public boolean isPriceChanged() { return priceChanged; }
}
