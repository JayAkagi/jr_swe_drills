package com.practice.model;

import java.util.List;

public class Transcript {
    private final String studentId;
    private final String studentName;
    private final List<TranscriptEntry> entries;
    private final double weightedGPA;
    private final String classification;
    private final int totalCreditsAttempted;
    private final int totalCreditsPassed;

    public Transcript(String studentId, String studentName, List<TranscriptEntry> entries,
                      double weightedGPA, String classification,
                      int totalCreditsAttempted, int totalCreditsPassed) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.entries = entries;
        this.weightedGPA = weightedGPA;
        this.classification = classification;
        this.totalCreditsAttempted = totalCreditsAttempted;
        this.totalCreditsPassed = totalCreditsPassed;
    }

    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public List<TranscriptEntry> getEntries() { return entries; }
    public double getWeightedGPA() { return weightedGPA; }
    public String getClassification() { return classification; }
    public int getTotalCreditsAttempted() { return totalCreditsAttempted; }
    public int getTotalCreditsPassed() { return totalCreditsPassed; }
}
