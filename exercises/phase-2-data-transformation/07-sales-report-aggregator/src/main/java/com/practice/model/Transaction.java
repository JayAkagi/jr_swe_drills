package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private final String transactionId;
    private final String region;
    private final String productName;
    private final int quantitySold;
    private final BigDecimal unitPriceGBP;
    private final LocalDate saleDate;

    public Transaction(String transactionId, String region, String productName,
                       int quantitySold, BigDecimal unitPriceGBP, LocalDate saleDate) {
        this.transactionId = transactionId;
        this.region = region;
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.unitPriceGBP = unitPriceGBP;
        this.saleDate = saleDate;
    }

    public String getTransactionId() { return transactionId; }
    public String getRegion() { return region; }
    public String getProductName() { return productName; }
    public int getQuantitySold() { return quantitySold; }
    public BigDecimal getUnitPriceGBP() { return unitPriceGBP; }
    public LocalDate getSaleDate() { return saleDate; }
}
