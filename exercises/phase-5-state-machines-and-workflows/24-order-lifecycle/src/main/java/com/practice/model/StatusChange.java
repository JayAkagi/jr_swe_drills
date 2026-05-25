package com.practice.model;

import java.time.LocalDateTime;

public class StatusChange {

    private final OrderStatus from;
    private final OrderStatus to;
    private final LocalDateTime changedAt;
    private final String reason;

    public StatusChange(OrderStatus from, OrderStatus to, LocalDateTime changedAt, String reason) {
        this.from = from;
        this.to = to;
        this.changedAt = changedAt;
        this.reason = reason;
    }

    public OrderStatus getFrom() {
        return from;
    }

    public OrderStatus getTo() {
        return to;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public String getReason() {
        return reason;
    }
}
