package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SalesReport {
    private final LocalDate reportDate;
    private final List<RegionReport> regions;
    private final BigDecimal grandTotalRevenueGBP;

    public SalesReport(LocalDate reportDate, List<RegionReport> regions, BigDecimal grandTotalRevenueGBP) {
        this.reportDate = reportDate;
        this.regions = regions;
        this.grandTotalRevenueGBP = grandTotalRevenueGBP;
    }

    public LocalDate getReportDate() { return reportDate; }
    public List<RegionReport> getRegions() { return regions; }
    public BigDecimal getGrandTotalRevenueGBP() { return grandTotalRevenueGBP; }
}
