package com.practice.model;

import java.time.LocalDateTime;

public class Ticket {
    private final String ticketId;
    private final Priority priority;
    private final LocalDateTime createdAt;
    private final LocalDateTime resolvedAt;

    public Ticket(String ticketId, Priority priority, LocalDateTime createdAt, LocalDateTime resolvedAt) {
        this.ticketId = ticketId;
        this.priority = priority;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
    }

    public String getTicketId() { return ticketId; }
    public Priority getPriority() { return priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
}
