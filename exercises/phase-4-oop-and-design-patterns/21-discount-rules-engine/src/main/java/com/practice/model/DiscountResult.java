package com.practice.model;

public class DiscountResult {

    private final int discountPercent;
    private final String reason;

    public DiscountResult(int discountPercent, String reason) {
        this.discountPercent = discountPercent;
        this.reason = reason;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public String getReason() {
        return reason;
    }
}
