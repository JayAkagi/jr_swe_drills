package com.practice.service;

import com.practice.model.DiscountResult;
import com.practice.model.OrderContext;

public abstract class DiscountRule {

    private DiscountRule next;

    public abstract DiscountResult apply(OrderContext ctx);

    public DiscountRule getNext() {
        return next;
    }

    public void setNext(DiscountRule next) {
        this.next = next;
    }

    protected DiscountResult passToNext(OrderContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
