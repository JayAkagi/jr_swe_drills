package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SaleRecord {
    private final String salespersonId;
    private final LocalDate saleDate;
    private final BigDecimal dealValueGBP;
    private final ProductLine productLine;

    public SaleRecord(String salespersonId, LocalDate saleDate, BigDecimal dealValueGBP, ProductLine productLine) {
        this.salespersonId = salespersonId;
        this.saleDate = saleDate;
        this.dealValueGBP = dealValueGBP;
        this.productLine = productLine;
    }

    public String getSalespersonId() { return salespersonId; }
    public LocalDate getSaleDate() { return saleDate; }
    public BigDecimal getDealValueGBP() { return dealValueGBP; }
    public ProductLine getProductLine() { return productLine; }
}
