package com.practice.model;

import java.math.BigDecimal;

public class LineItem {

    private final String description;
    private final int quantity;
    private final BigDecimal unitPriceGBP;
    private final int vatRatePercent;
    private final LineItemCategory category;

    public LineItem(String description, int quantity, BigDecimal unitPriceGBP, int vatRatePercent, LineItemCategory category) {
        this.description = description;
        this.quantity = quantity;
        this.unitPriceGBP = unitPriceGBP;
        this.vatRatePercent = vatRatePercent;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPriceGBP() {
        return unitPriceGBP;
    }

    public int getVatRatePercent() {
        return vatRatePercent;
    }

    public LineItemCategory getCategory() {
        return category;
    }
}
