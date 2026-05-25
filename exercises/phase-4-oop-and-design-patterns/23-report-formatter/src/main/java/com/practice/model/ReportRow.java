package com.practice.model;

import java.math.BigDecimal;

public class ReportRow {

    private final String label;
    private final BigDecimal valueGBP;

    public ReportRow(String label, BigDecimal valueGBP) {
        this.label = label;
        this.valueGBP = valueGBP;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getValueGBP() {
        return valueGBP;
    }
}
