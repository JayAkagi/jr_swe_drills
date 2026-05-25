package com.practice.model;

import java.time.LocalDate;

public class RecurringEvent {
    private final String name;
    private final LocalDate startDate;
    private final RecurrenceType recurrenceType;
    private final int interval;
    private final LocalDate endDate;

    public RecurringEvent(String name, LocalDate startDate, RecurrenceType recurrenceType, int interval, LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.recurrenceType = recurrenceType;
        this.interval = interval;
        this.endDate = endDate;
    }

    public String getName() { return name; }
    public LocalDate getStartDate() { return startDate; }
    public RecurrenceType getRecurrenceType() { return recurrenceType; }
    public int getInterval() { return interval; }
    public LocalDate getEndDate() { return endDate; }
}
