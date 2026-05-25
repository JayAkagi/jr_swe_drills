package com.practice.service;

import com.practice.model.SalesReport;
import com.practice.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalesReportGeneratorTest {

    private SalesReportGenerator generator;
    private final LocalDate REPORT_DATE = LocalDate.of(2024, 6, 1);

    @BeforeEach
    void setUp() {
        generator = new SalesReportGenerator();
    }

    @Test
    void shouldReturnEmptyReport_whenTransactionsIsEmpty() {
        SalesReport report = generator.generate(List.of(), REPORT_DATE);
        assertNotNull(report);
        assertTrue(report.getRegions().isEmpty());
        assertEquals(0, BigDecimal.ZERO.compareTo(report.getGrandTotalRevenueGBP()));
    }

    @Test
    void shouldSetReportDate_whenGenerating() {
        SalesReport report = generator.generate(List.of(), REPORT_DATE);
        assertEquals(REPORT_DATE, report.getReportDate());
    }

    @Test
    void shouldProduceOneRegionOneProduct_whenSingleTransaction() {
        Transaction t = new Transaction("T1", "North", "Widget", 2, new BigDecimal("5.00"), LocalDate.of(2024, 1, 1));
        SalesReport report = generator.generate(List.of(t), REPORT_DATE);
        assertEquals(1, report.getRegions().size());
        assertEquals("North", report.getRegions().get(0).getRegion());
        assertEquals(1, report.getRegions().get(0).getProducts().size());
        assertEquals("Widget", report.getRegions().get(0).getProducts().get(0).getProductName());
    }

    @Test
    void shouldComputeProductRevenue_whenSingleTransaction() {
        Transaction t = new Transaction("T1", "North", "Widget", 3, new BigDecimal("10.00"), LocalDate.of(2024, 1, 1));
        SalesReport report = generator.generate(List.of(t), REPORT_DATE);
        BigDecimal expected = new BigDecimal("30.00");
        assertEquals(0, expected.compareTo(report.getRegions().get(0).getProducts().get(0).getTotalRevenueGBP()));
    }

    @Test
    void shouldAggregateQuantityAndRevenue_whenMultipleTransactionsSameProduct() {
        Transaction t1 = new Transaction("T1", "North", "Widget", 2, new BigDecimal("5.00"), LocalDate.of(2024, 1, 1));
        Transaction t2 = new Transaction("T2", "North", "Widget", 3, new BigDecimal("5.00"), LocalDate.of(2024, 1, 2));
        SalesReport report = generator.generate(List.of(t1, t2), REPORT_DATE);
        var product = report.getRegions().get(0).getProducts().get(0);
        assertEquals(5, product.getTotalQuantity());
        assertEquals(0, new BigDecimal("25.00").compareTo(product.getTotalRevenueGBP()));
    }

    @Test
    void shouldSortRegionsAlphabetically_whenMultipleRegions() {
        Transaction t1 = new Transaction("T1", "South", "Widget", 1, new BigDecimal("1.00"), LocalDate.of(2024, 1, 1));
        Transaction t2 = new Transaction("T2", "North", "Gadget", 1, new BigDecimal("2.00"), LocalDate.of(2024, 1, 1));
        Transaction t3 = new Transaction("T3", "East", "Item", 1, new BigDecimal("3.00"), LocalDate.of(2024, 1, 1));
        SalesReport report = generator.generate(List.of(t1, t2, t3), REPORT_DATE);
        List<String> regions = report.getRegions().stream().map(r -> r.getRegion()).toList();
        assertEquals(List.of("East", "North", "South"), regions);
    }

    @Test
    void shouldSortProductsAlphabetically_whenMultipleProductsInRegion() {
        Transaction t1 = new Transaction("T1", "North", "Zebra", 1, new BigDecimal("1.00"), LocalDate.of(2024, 1, 1));
        Transaction t2 = new Transaction("T2", "North", "Apple", 1, new BigDecimal("2.00"), LocalDate.of(2024, 1, 1));
        Transaction t3 = new Transaction("T3", "North", "Mango", 1, new BigDecimal("3.00"), LocalDate.of(2024, 1, 1));
        SalesReport report = generator.generate(List.of(t1, t2, t3), REPORT_DATE);
        List<String> products = report.getRegions().get(0).getProducts().stream().map(p -> p.getProductName()).toList();
        assertEquals(List.of("Apple", "Mango", "Zebra"), products);
    }

    @Test
    void shouldComputeRegionTotal_whenMultipleProducts() {
        Transaction t1 = new Transaction("T1", "North", "Widget", 2, new BigDecimal("5.00"), LocalDate.of(2024, 1, 1));
        Transaction t2 = new Transaction("T2", "North", "Gadget", 4, new BigDecimal("3.00"), LocalDate.of(2024, 1, 1));
        SalesReport report = generator.generate(List.of(t1, t2), REPORT_DATE);
        BigDecimal expected = new BigDecimal("22.00");
        assertEquals(0, expected.compareTo(report.getRegions().get(0).getTotalRevenueGBP()));
    }

    @Test
    void shouldComputeGrandTotal_whenMultipleRegions() {
        Transaction t1 = new Transaction("T1", "North", "Widget", 2, new BigDecimal("10.00"), LocalDate.of(2024, 1, 1));
        Transaction t2 = new Transaction("T2", "South", "Gadget", 3, new BigDecimal("5.00"), LocalDate.of(2024, 1, 1));
        SalesReport report = generator.generate(List.of(t1, t2), REPORT_DATE);
        BigDecimal expected = new BigDecimal("35.00");
        assertEquals(0, expected.compareTo(report.getGrandTotalRevenueGBP()));
    }

    @Test
    void shouldSeparateProductsByRegion_whenSameProductInDifferentRegions() {
        Transaction t1 = new Transaction("T1", "North", "Widget", 2, new BigDecimal("5.00"), LocalDate.of(2024, 1, 1));
        Transaction t2 = new Transaction("T2", "South", "Widget", 3, new BigDecimal("5.00"), LocalDate.of(2024, 1, 1));
        SalesReport report = generator.generate(List.of(t1, t2), REPORT_DATE);
        assertEquals(2, report.getRegions().size());
        assertEquals(1, report.getRegions().get(0).getProducts().size());
        assertEquals(1, report.getRegions().get(1).getProducts().size());
    }

    @Test
    void shouldComputeRevenueAsQuantityTimesUnitPrice_perTransaction() {
        Transaction t1 = new Transaction("T1", "North", "Widget", 5, new BigDecimal("3.50"), LocalDate.of(2024, 1, 1));
        Transaction t2 = new Transaction("T2", "North", "Widget", 2, new BigDecimal("4.00"), LocalDate.of(2024, 1, 2));
        SalesReport report = generator.generate(List.of(t1, t2), REPORT_DATE);
        BigDecimal expected = new BigDecimal("25.50");
        assertEquals(0, expected.compareTo(report.getRegions().get(0).getProducts().get(0).getTotalRevenueGBP()));
    }

    @Test
    void shouldHaveCorrectTotalQuantity_whenAggregatingMultipleTransactions() {
        Transaction t1 = new Transaction("T1", "East", "Bolt", 10, new BigDecimal("1.00"), LocalDate.of(2024, 1, 1));
        Transaction t2 = new Transaction("T2", "East", "Bolt", 7, new BigDecimal("1.00"), LocalDate.of(2024, 1, 2));
        Transaction t3 = new Transaction("T3", "East", "Bolt", 3, new BigDecimal("1.00"), LocalDate.of(2024, 1, 3));
        SalesReport report = generator.generate(List.of(t1, t2, t3), REPORT_DATE);
        assertEquals(20, report.getRegions().get(0).getProducts().get(0).getTotalQuantity());
    }
}
