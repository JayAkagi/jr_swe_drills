package com.practice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderPlacedEvent extends Event {

    private final String orderId;
    private final String customerId;
    private final BigDecimal totalGBP;

    public OrderPlacedEvent(String orderId, String customerId, BigDecimal totalGBP) {
        super("ORDER_PLACED", LocalDateTime.now());
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalGBP = totalGBP;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getTotalGBP() {
        return totalGBP;
    }
}
