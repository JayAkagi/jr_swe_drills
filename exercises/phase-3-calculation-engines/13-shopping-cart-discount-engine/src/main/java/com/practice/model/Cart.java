package com.practice.model;

import java.util.List;

public class Cart {
    private final List<CartItem> items;
    private final List<Voucher> appliedVouchers;

    public Cart(List<CartItem> items, List<Voucher> appliedVouchers) {
        this.items = items;
        this.appliedVouchers = appliedVouchers;
    }

    public List<CartItem> getItems() { return items; }
    public List<Voucher> getAppliedVouchers() { return appliedVouchers; }
}
