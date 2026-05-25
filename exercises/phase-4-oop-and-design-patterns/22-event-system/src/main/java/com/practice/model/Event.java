package com.practice.model;

import java.time.LocalDateTime;

public abstract class Event {

    private final String eventType;
    private final LocalDateTime occurredAt;

    public Event(String eventType, LocalDateTime occurredAt) {
        this.eventType = eventType;
        this.occurredAt = occurredAt;
    }

    public String getEventType() {
        return eventType;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
