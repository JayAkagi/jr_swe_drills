package com.practice.model;

import java.time.LocalDate;
import java.util.List;

public class Timesheet {

    private final String employeeName;
    private final LocalDate weekStartDate;
    private final LocalDate submissionDate;
    private final List<TimeEntry> entries;

    public Timesheet(String employeeName, LocalDate weekStartDate, LocalDate submissionDate, List<TimeEntry> entries) {
        this.employeeName = employeeName;
        this.weekStartDate = weekStartDate;
        this.submissionDate = submissionDate;
        this.entries = entries;
    }

    public String getEmployeeName() { return employeeName; }
    public LocalDate getWeekStartDate() { return weekStartDate; }
    public LocalDate getSubmissionDate() { return submissionDate; }
    public List<TimeEntry> getEntries() { return entries; }
}
