package com.practice.model;

import java.time.LocalDate;

public class SubscriptionEvent {

    private final String event;
    private final LocalDate occurredAt;
    private final String note;

    public SubscriptionEvent(String event, LocalDate occurredAt, String note) {
        this.event = event;
        this.occurredAt = occurredAt;
        this.note = note;
    }

    public String getEvent() {
        return event;
    }

    public LocalDate getOccurredAt() {
        return occurredAt;
    }

    public String getNote() {
        return note;
    }
}
