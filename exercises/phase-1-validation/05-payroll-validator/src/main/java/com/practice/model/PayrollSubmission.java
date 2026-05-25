package com.practice.model;

import java.time.LocalDate;
import java.util.List;

public class PayrollSubmission {

    private final String submittedBy;
    private final LocalDate submissionDate;
    private final List<PayrollEntry> entries;

    public PayrollSubmission(String submittedBy, LocalDate submissionDate, List<PayrollEntry> entries) {
        this.submittedBy = submittedBy;
        this.submissionDate = submissionDate;
        this.entries = entries;
    }

    public String getSubmittedBy() { return submittedBy; }
    public LocalDate getSubmissionDate() { return submissionDate; }
    public List<PayrollEntry> getEntries() { return entries; }
}
