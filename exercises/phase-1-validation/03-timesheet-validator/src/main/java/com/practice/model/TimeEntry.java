package com.practice.model;

import java.time.LocalDate;

public class TimeEntry {

    private final LocalDate date;
    private final double hoursWorked;
    private final String projectCode;
    private final EntryType entryType;

    public TimeEntry(LocalDate date, double hoursWorked, String projectCode, EntryType entryType) {
        this.date = date;
        this.hoursWorked = hoursWorked;
        this.projectCode = projectCode;
        this.entryType = entryType;
    }

    public LocalDate getDate() { return date; }
    public double getHoursWorked() { return hoursWorked; }
    public String getProjectCode() { return projectCode; }
    public EntryType getEntryType() { return entryType; }
}
