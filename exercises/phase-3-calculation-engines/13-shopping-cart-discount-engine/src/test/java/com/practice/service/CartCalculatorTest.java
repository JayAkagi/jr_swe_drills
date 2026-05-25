package com.practice.service;

import com.practice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartCalculatorTest {

    private CartCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new CartCalculator();
    }

    @Test
    void shouldApplyNoVAT_whenCategoryIsFood() {
        CartItem item = new CartItem("p1", "Bread", ProductCategory.FOOD, new BigDecimal("2.00"), 1);
        Cart cart = new Cart(List.of(item), List.of());
        CartTotal total = calculator.calculate(cart);
        assertEquals(0, new BigDecimal("2.00").compareTo(total.getSubtotalGBP()));
    }

    @Test
    void shouldApplyNoVAT_whenCategoryIsBooks() {
        CartItem item = new CartItem("p2", "Novel", ProductCategory.BOOKS, new BigDecimal("10.00"), 1);
        Cart cart = new Cart(List.of(item), List.of());
        CartTotal total = calculator.calculate(cart);
        assertEquals(0, new BigDecimal("10.00").compareTo(total.getSubtotalGBP()));
    }

    @Test
    void shouldApply20PercentVAT_whenCategoryIsElectronics() {
        CartItem item = new CartItem("p3", "Headphones", ProductCategory.ELECTRONICS, new BigDecimal("100.00"), 1);
        Cart cart = new Cart(List.of(item), List.of());
        CartTotal total = calculator.calculate(cart);
        assertEquals(0, new BigDecimal("120.00").compareTo(total.getSubtotalGBP()));
    }

    @Test
    void shouldApply20PercentVAT_whenCategoryIsClothing() {
        CartItem item = new CartItem("p4", "Shirt", ProductCategory.CLOTHING, new BigDecimal("50.00"), 2);
        Cart cart = new Cart(List.of(item), List.of());
        CartTotal total = calculator.calculate(cart);
        assertEquals(0, new BigDecimal("120.00").compareTo(total.getSubtotalGBP()));
    }

    @Test
    void shouldApplyPercentageOffVoucher_whenVoucherTypeIsPercentageOff() {
        CartItem item = new CartItem("p5", "Laptop", ProductCategory.ELECTRONICS, new BigDecimal("500.00"), 1);
        Voucher voucher = new Voucher("SAVE10", VoucherType.PERCENTAGE_OFF, new BigDecimal("10"));
        Cart cart = new Cart(List.of(item), List.of(voucher));
        CartTotal total = calculator.calculate(cart);
        assertEquals(0, new BigDecimal("600.00").compareTo(total.getSubtotalGBP()));
        assertEquals(0, new BigDecimal("60.00").compareTo(total.getTotalDiscountGBP()));
        assertEquals(0, new BigDecimal("540.00").compareTo(total.getFinalTotalGBP()));
    }

    @Test
    void shouldApplyFixedAmountOffVoucher_whenVoucherTypeIsFixedAmountOff() {
        CartItem item = new CartItem("p6", "Rice", ProductCategory.FOOD, new BigDecimal("20.00"), 1);
        Voucher voucher = new Voucher("FLAT5", VoucherType.FIXED_AMOUNT_OFF, new BigDecimal("5.00"));
        Cart cart = new Cart(List.of(item), List.of(voucher));
        CartTotal total = calculator.calculate(cart);
        assertEquals(0, new BigDecimal("15.00").compareTo(total.getFinalTotalGBP()));
    }

    @Test
    void shouldGiveOneFreeItem_whenBogoQuantityIsTwo() {
        CartItem item = new CartItem("p7", "Book", ProductCategory.BOOKS, new BigDecimal("10.00"), 2);
        Voucher voucher = new Voucher("BOGO", VoucherType.BUY_ONE_GET_ONE, BigDecimal.ZERO);
        Cart cart = new Cart(List.of(item), List.of(voucher));
        CartTotal total = calculator.calculate(cart);
        assertEquals(0, new BigDecimal("10.00").compareTo(total.getTotalDiscountGBP()));
        assertEquals(0, new BigDecimal("10.00").compareTo(total.getFinalTotalGBP()));
    }

    @Test
    void shouldGiveOneFreeItem_whenBogoQuantityIsThree() {
        CartItem item = new CartItem("p8", "Book", ProductCategory.BOOKS, new BigDecimal("10.00"), 3);
        Voucher voucher = new Voucher("BOGO", VoucherType.BUY_ONE_GET_ONE, BigDecimal.ZERO);
        Cart cart = new Cart(List.of(item), List.of(voucher));
        CartTotal total = calculator.calculate(cart);
        assertEquals(0, new BigDecimal("10.00").compareTo(total.getTotalDiscountGBP()));
        assertEquals(0, new BigDecimal("20.00").compareTo(total.getFinalTotalGBP()));
    }

    @Test
    void shouldGiveTwoFreeItems_whenBogoQuantityIsFour() {
        CartItem item = new CartItem("p9", "Book", ProductCategory.BOOKS, new BigDecimal("10.00"), 4);
        Voucher voucher = new Voucher("BOGO", VoucherType.BUY_ONE_GET_ONE, BigDecimal.ZERO);
        Cart cart = new Cart(List.of(item), List.of(voucher));
        CartTotal total = calculator.calculate(cart);
        assertEquals(0, new BigDecimal("20.00").compareTo(total.getTotalDiscountGBP()));
        assertEquals(0, new BigDecimal("20.00").compareTo(total.getFinalTotalGBP()));
    }

    @Test
    void shouldStackMultipleVouchers_whenBothBogoAndPercentageApplied() {
        CartItem item = new CartItem("p10", "Book", ProductCategory.BOOKS, new BigDecimal("10.00"), 2);
        Voucher bogo = new Voucher("BOGO", VoucherType.BUY_ONE_GET_ONE, BigDecimal.ZERO);
        Voucher pct = new Voucher("SAVE10", VoucherType.PERCENTAGE_OFF, new BigDecimal("10"));
        Cart cart = new Cart(List.of(item), List.of(bogo, pct));
        CartTotal total = calculator.calculate(cart);
        BigDecimal subtotal = new BigDecimal("20.00");
        BigDecimal bogoDiscount = new BigDecimal("10.00");
        BigDecimal afterBogo = subtotal.subtract(bogoDiscount);
        BigDecimal pctDiscount = afterBogo.multiply(new BigDecimal("10")).divide(new BigDecimal("100")).setScale(2, java.math.RoundingMode.HALF_UP);
        BigDecimal expectedTotal = subtotal.subtract(bogoDiscount).subtract(pctDiscount);
        assertEquals(0, expectedTotal.compareTo(total.getFinalTotalGBP()));
    }

    @Test
    void shouldClampFinalTotalToZero_whenDiscountsExceedSubtotal() {
        CartItem item = new CartItem("p11", "Snack", ProductCategory.FOOD, new BigDecimal("5.00"), 1);
        Voucher voucher = new Voucher("MEGA", VoucherType.FIXED_AMOUNT_OFF, new BigDecimal("100.00"));
        Cart cart = new Cart(List.of(item), List.of(voucher));
        CartTotal total = calculator.calculate(cart);
        assertEquals(0, BigDecimal.ZERO.compareTo(total.getFinalTotalGBP()));
    }

    @Test
    void shouldPopulateDiscountBreakdown_whenVouchersAreApplied() {
        CartItem item = new CartItem("p12", "Fruit", ProductCategory.FOOD, new BigDecimal("10.00"), 1);
        Voucher voucher = new Voucher("SAVE20", VoucherType.PERCENTAGE_OFF, new BigDecimal("20"));
        Cart cart = new Cart(List.of(item), List.of(voucher));
        CartTotal total = calculator.calculate(cart);
        assertFalse(total.getDiscountBreakdown().isEmpty());
    }

    @Test
    void shouldHaveTotalDiscountEqualToSumOfBreakdown_whenMultipleDiscountsApplied() {
        CartItem item = new CartItem("p13", "Food", ProductCategory.FOOD, new BigDecimal("50.00"), 2);
        Voucher pct = new Voucher("PCT", VoucherType.PERCENTAGE_OFF, new BigDecimal("10"));
        Voucher fixed = new Voucher("FIXED", VoucherType.FIXED_AMOUNT_OFF, new BigDecimal("5.00"));
        Cart cart = new Cart(List.of(item), List.of(pct, fixed));
        CartTotal total = calculator.calculate(cart);
        BigDecimal sumOfBreakdown = total.getDiscountBreakdown().stream()
                .map(DiscountLine::getAmountGBP)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(0, sumOfBreakdown.compareTo(total.getTotalDiscountGBP()));
    }
}
