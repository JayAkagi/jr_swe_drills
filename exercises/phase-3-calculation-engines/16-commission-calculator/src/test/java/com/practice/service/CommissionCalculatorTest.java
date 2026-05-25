package com.practice.service;

import com.practice.model.CommissionResult;
import com.practice.model.ProductLine;
import com.practice.model.SaleRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommissionCalculatorTest {

    private CommissionCalculator calculator;
    private static final YearMonth PERIOD = YearMonth.of(2024, 3);
    private static final LocalDate IN_PERIOD = LocalDate.of(2024, 3, 15);
    private static final LocalDate OUT_OF_PERIOD = LocalDate.of(2024, 2, 15);

    @BeforeEach
    void setUp() {
        calculator = new CommissionCalculator();
    }

    @Test
    void shouldExcludeSales_whenSaleDateIsOutsidePeriod() {
        SaleRecord sale = new SaleRecord("SP1", OUT_OF_PERIOD, new BigDecimal("5000.00"), ProductLine.STANDARD);
        Map<String, BigDecimal> targets = Map.of("SP1", new BigDecimal("10000.00"));
        List<CommissionResult> results = calculator.calculate(List.of(sale), targets, PERIOD);
        CommissionResult result = results.stream().filter(r -> r.getSalespersonId().equals("SP1")).findFirst().orElse(null);
        assertTrue(result == null || result.getTotalSalesGBP().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    void shouldCalculateTier1Commission_whenSingleStandardSaleIsUnder10000() {
        SaleRecord sale = new SaleRecord("SP1", IN_PERIOD, new BigDecimal("8000.00"), ProductLine.STANDARD);
        Map<String, BigDecimal> targets = Map.of("SP1", new BigDecimal("10000.00"));
        List<CommissionResult> results = calculator.calculate(List.of(sale), targets, PERIOD);
        CommissionResult result = results.get(0);
        BigDecimal expectedCommission = new BigDecimal("8000.00").multiply(new BigDecimal("0.05")).setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, expectedCommission.compareTo(result.getCommissionGBP()));
        assertEquals("Tier 1", result.getTierReached());
    }

    @Test
    void shouldCalculateTier2Commission_whenSaleCrossesTier1Boundary() {
        SaleRecord sale = new SaleRecord("SP1", IN_PERIOD, new BigDecimal("20000.00"), ProductLine.STANDARD);
        Map<String, BigDecimal> targets = Map.of("SP1", new BigDecimal("20000.00"));
        List<CommissionResult> results = calculator.calculate(List.of(sale), targets, PERIOD);
        CommissionResult result = results.get(0);
        BigDecimal tier1 = new BigDecimal("10000.00").multiply(new BigDecimal("0.05"));
        BigDecimal tier2 = new BigDecimal("10000.00").multiply(new BigDecimal("0.08"));
        BigDecimal expectedCommission = tier1.add(tier2).setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, expectedCommission.compareTo(result.getCommissionGBP()));
        assertEquals("Tier 2", result.getTierReached());
    }

    @Test
    void shouldApplyPremiumMultiplier_whenDealIsPremium() {
        SaleRecord premiumSale = new SaleRecord("SP1", IN_PERIOD, new BigDecimal("8000.00"), ProductLine.PREMIUM);
        SaleRecord standardSale = new SaleRecord("SP2", IN_PERIOD, new BigDecimal("8000.00"), ProductLine.STANDARD);
        Map<String, BigDecimal> targets = Map.of("SP1", new BigDecimal("10000.00"), "SP2", new BigDecimal("10000.00"));
        List<CommissionResult> results = calculator.calculate(List.of(premiumSale, standardSale), targets, PERIOD);
        CommissionResult premium = results.stream().filter(r -> r.getSalespersonId().equals("SP1")).findFirst().orElseThrow();
        CommissionResult standard = results.stream().filter(r -> r.getSalespersonId().equals("SP2")).findFirst().orElseThrow();
        assertTrue(premium.getCommissionGBP().compareTo(standard.getCommissionGBP()) > 0);
    }

    @Test
    void shouldApplyEnterpriseMultiplier_whenDealIsEnterprise() {
        SaleRecord enterpriseSale = new SaleRecord("SP1", IN_PERIOD, new BigDecimal("8000.00"), ProductLine.ENTERPRISE);
        SaleRecord premiumSale = new SaleRecord("SP2", IN_PERIOD, new BigDecimal("8000.00"), ProductLine.PREMIUM);
        Map<String, BigDecimal> targets = Map.of("SP1", new BigDecimal("10000.00"), "SP2", new BigDecimal("10000.00"));
        List<CommissionResult> results = calculator.calculate(List.of(enterpriseSale, premiumSale), targets, PERIOD);
        CommissionResult enterprise = results.stream().filter(r -> r.getSalespersonId().equals("SP1")).findFirst().orElseThrow();
        CommissionResult premium = results.stream().filter(r -> r.getSalespersonId().equals("SP2")).findFirst().orElseThrow();
        assertTrue(enterprise.getCommissionGBP().compareTo(premium.getCommissionGBP()) > 0);
    }

    @Test
    void shouldSetCommissionToZero_whenAchievementIsBelow50Percent() {
        SaleRecord sale = new SaleRecord("SP1", IN_PERIOD, new BigDecimal("4000.00"), ProductLine.STANDARD);
        Map<String, BigDecimal> targets = Map.of("SP1", new BigDecimal("20000.00"));
        List<CommissionResult> results = calculator.calculate(List.of(sale), targets, PERIOD);
        CommissionResult result = results.get(0);
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getCommissionGBP()));
    }

    @Test
    void shouldCalculateAchievementPercent_whenTargetIsSet() {
        SaleRecord sale = new SaleRecord("SP1", IN_PERIOD, new BigDecimal("8000.00"), ProductLine.STANDARD);
        Map<String, BigDecimal> targets = Map.of("SP1", new BigDecimal("10000.00"));
        List<CommissionResult> results = calculator.calculate(List.of(sale), targets, PERIOD);
        CommissionResult result = results.get(0);
        assertEquals(0, new BigDecimal("80.00").compareTo(result.getAchievementPercent()));
    }

    @Test
    void shouldProduceSeparateResults_whenMultipleSalespersonsHaveSales() {
        SaleRecord sale1 = new SaleRecord("SP1", IN_PERIOD, new BigDecimal("5000.00"), ProductLine.STANDARD);
        SaleRecord sale2 = new SaleRecord("SP2", IN_PERIOD, new BigDecimal("7000.00"), ProductLine.STANDARD);
        Map<String, BigDecimal> targets = Map.of("SP1", new BigDecimal("10000.00"), "SP2", new BigDecimal("10000.00"));
        List<CommissionResult> results = calculator.calculate(List.of(sale1, sale2), targets, PERIOD);
        assertEquals(2, results.size());
    }

    @Test
    void shouldReachTier3_whenTotalSalesExceed25000() {
        SaleRecord sale = new SaleRecord("SP1", IN_PERIOD, new BigDecimal("30000.00"), ProductLine.STANDARD);
        Map<String, BigDecimal> targets = Map.of("SP1", new BigDecimal("30000.00"));
        List<CommissionResult> results = calculator.calculate(List.of(sale), targets, PERIOD);
        assertEquals("Tier 3", results.get(0).getTierReached());
    }

    @Test
    void shouldHaveTotalSalesEqualToSumOfInPeriodDeals() {
        SaleRecord sale1 = new SaleRecord("SP1", IN_PERIOD, new BigDecimal("3000.00"), ProductLine.STANDARD);
        SaleRecord sale2 = new SaleRecord("SP1", IN_PERIOD, new BigDecimal("4000.00"), ProductLine.STANDARD);
        SaleRecord sale3 = new SaleRecord("SP1", OUT_OF_PERIOD, new BigDecimal("9999.00"), ProductLine.STANDARD);
        Map<String, BigDecimal> targets = Map.of("SP1", new BigDecimal("10000.00"));
        List<CommissionResult> results = calculator.calculate(List.of(sale1, sale2, sale3), targets, PERIOD);
        CommissionResult result = results.stream().filter(r -> r.getSalespersonId().equals("SP1")).findFirst().orElseThrow();
        assertEquals(0, new BigDecimal("7000.00").compareTo(result.getTotalSalesGBP()));
    }

    @Test
    void shouldHandleZeroTarget_whenSalespersonHasNoTargetEntry() {
        SaleRecord sale = new SaleRecord("SP1", IN_PERIOD, new BigDecimal("5000.00"), ProductLine.STANDARD);
        List<CommissionResult> results = calculator.calculate(List.of(sale), Map.of(), PERIOD);
        CommissionResult result = results.stream().filter(r -> r.getSalespersonId().equals("SP1")).findFirst().orElseThrow();
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getTargetGBP()));
    }

    @Test
    void shouldReachTier1_whenTotalSalesIsExactly10000() {
        SaleRecord sale = new SaleRecord("SP1", IN_PERIOD, new BigDecimal("10000.00"), ProductLine.STANDARD);
        Map<String, BigDecimal> targets = Map.of("SP1", new BigDecimal("10000.00"));
        List<CommissionResult> results = calculator.calculate(List.of(sale), targets, PERIOD);
        assertEquals("Tier 1", results.get(0).getTierReached());
    }
}
