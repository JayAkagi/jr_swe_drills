package com.practice.service;

import com.practice.model.CustomerTier;
import com.practice.model.DiscountResult;
import com.practice.model.OrderContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DiscountEngineTest {

    private DiscountEngine engine;

    @BeforeEach
    void setUp() {
        engine = new DiscountEngine();
    }

    @Test
    void shouldApply30Percent_whenCustomerIsPlatinum() {
        OrderContext ctx = new OrderContext("cust-1", new BigDecimal("50.00"), false, 2, CustomerTier.PLATINUM);
        DiscountResult result = engine.calculate(ctx);
        assertEquals(30, result.getDiscountPercent());
    }

    @Test
    void shouldApply20Percent_whenGoldLoyaltyMemberSpendingOver200() {
        OrderContext ctx = new OrderContext("cust-2", new BigDecimal("250.00"), true, 2, CustomerTier.GOLD);
        DiscountResult result = engine.calculate(ctx);
        assertEquals(20, result.getDiscountPercent());
    }

    @Test
    void shouldApply15Percent_whenOrderHas10OrMoreItems() {
        OrderContext ctx = new OrderContext("cust-3", new BigDecimal("80.00"), false, 10, CustomerTier.SILVER);
        DiscountResult result = engine.calculate(ctx);
        assertEquals(15, result.getDiscountPercent());
    }

    @Test
    void shouldApply5Percent_whenNoCriteriaMatch() {
        OrderContext ctx = new OrderContext("cust-4", new BigDecimal("50.00"), false, 3, CustomerTier.BRONZE);
        DiscountResult result = engine.calculate(ctx);
        assertEquals(5, result.getDiscountPercent());
    }

    @Test
    void shouldApply30Percent_andSkipOtherRules_whenPlatinum() {
        OrderContext ctx = new OrderContext("cust-5", new BigDecimal("500.00"), true, 15, CustomerTier.PLATINUM);
        DiscountResult result = engine.calculate(ctx);
        assertEquals(30, result.getDiscountPercent());
    }

    @Test
    void shouldNotApplyLoyaltyDiscount_whenOrderValueIsExactly200() {
        OrderContext ctx = new OrderContext("cust-6", new BigDecimal("200.00"), true, 2, CustomerTier.GOLD);
        DiscountResult result = engine.calculate(ctx);
        assertNotEquals(20, result.getDiscountPercent());
    }

    @Test
    void shouldNotApplyBulkDiscount_whenItemCountIs9() {
        OrderContext ctx = new OrderContext("cust-7", new BigDecimal("80.00"), false, 9, CustomerTier.BRONZE);
        DiscountResult result = engine.calculate(ctx);
        assertNotEquals(15, result.getDiscountPercent());
    }

    @Test
    void shouldApply15Percent_whenItemCountIsExactly10() {
        OrderContext ctx = new OrderContext("cust-8", new BigDecimal("80.00"), false, 10, CustomerTier.BRONZE);
        DiscountResult result = engine.calculate(ctx);
        assertEquals(15, result.getDiscountPercent());
    }

    @Test
    void shouldAlwaysReturnNonNull_forAnyContext() {
        OrderContext ctx = new OrderContext("cust-9", new BigDecimal("10.00"), false, 1, CustomerTier.BRONZE);
        DiscountResult result = engine.calculate(ctx);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNonNullReason_inResult() {
        OrderContext ctx = new OrderContext("cust-10", new BigDecimal("50.00"), false, 2, CustomerTier.SILVER);
        DiscountResult result = engine.calculate(ctx);
        assertNotNull(result.getReason());
        assertFalse(result.getReason().isBlank());
    }

    @Test
    void shouldNotApplyLoyaltyDiscount_whenNotLoyaltyMember() {
        OrderContext ctx = new OrderContext("cust-11", new BigDecimal("300.00"), false, 2, CustomerTier.GOLD);
        DiscountResult result = engine.calculate(ctx);
        assertNotEquals(20, result.getDiscountPercent());
    }

    @Test
    void shouldApply5Percent_asStandardFallback_forSilverNonLoyaltyFewItems() {
        OrderContext ctx = new OrderContext("cust-12", new BigDecimal("100.00"), false, 5, CustomerTier.SILVER);
        DiscountResult result = engine.calculate(ctx);
        assertEquals(5, result.getDiscountPercent());
    }
}
