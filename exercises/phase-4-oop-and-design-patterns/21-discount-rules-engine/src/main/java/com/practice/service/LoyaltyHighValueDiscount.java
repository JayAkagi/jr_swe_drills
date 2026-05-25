package com.practice.service;

import com.practice.model.DiscountResult;
import com.practice.model.OrderContext;

public class LoyaltyHighValueDiscount extends DiscountRule {

    @Override
    public DiscountResult apply(OrderContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
