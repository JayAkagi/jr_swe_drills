package com.practice.model;

import java.time.LocalDate;

public class EventOccurrence {
    private final String eventName;
    private final LocalDate occurrenceDate;

    public EventOccurrence(String eventName, LocalDate occurrenceDate) {
        this.eventName = eventName;
        this.occurrenceDate = occurrenceDate;
    }

    public String getEventName() { return eventName; }
    public LocalDate getOccurrenceDate() { return occurrenceDate; }
}
