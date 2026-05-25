package com.practice.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final String orderId;
    private OrderStatus status;
    private List<StatusChange> history;

    public Order(String orderId) {
        this.orderId = orderId;
        this.status = OrderStatus.PENDING;
        this.history = new ArrayList<>();
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<StatusChange> getHistory() {
        return history;
    }
}
