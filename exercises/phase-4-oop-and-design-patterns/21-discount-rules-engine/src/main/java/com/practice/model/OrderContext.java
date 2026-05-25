package com.practice.model;

import java.math.BigDecimal;

public class OrderContext {

    private final String customerId;
    private final BigDecimal orderValueGBP;
    private final boolean isLoyaltyMember;
    private final int itemCount;
    private final CustomerTier customerTier;

    public OrderContext(String customerId, BigDecimal orderValueGBP, boolean isLoyaltyMember,
                        int itemCount, CustomerTier customerTier) {
        this.customerId = customerId;
        this.orderValueGBP = orderValueGBP;
        this.isLoyaltyMember = isLoyaltyMember;
        this.itemCount = itemCount;
        this.customerTier = customerTier;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getOrderValueGBP() {
        return orderValueGBP;
    }

    public boolean isLoyaltyMember() {
        return isLoyaltyMember;
    }

    public int getItemCount() {
        return itemCount;
    }

    public CustomerTier getCustomerTier() {
        return customerTier;
    }
}
