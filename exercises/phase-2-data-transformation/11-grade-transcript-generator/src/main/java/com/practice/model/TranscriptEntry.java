package com.practice.model;

public class TranscriptEntry {
    private final String moduleCode;
    private final String moduleName;
    private final int credits;
    private final double scorePercent;
    private final String grade;
    private final double gradePoints;

    public TranscriptEntry(String moduleCode, String moduleName, int credits,
                           double scorePercent, String grade, double gradePoints) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.credits = credits;
        this.scorePercent = scorePercent;
        this.grade = grade;
        this.gradePoints = gradePoints;
    }

    public String getModuleCode() { return moduleCode; }
    public String getModuleName() { return moduleName; }
    public int getCredits() { return credits; }
    public double getScorePercent() { return scorePercent; }
    public String getGrade() { return grade; }
    public double getGradePoints() { return gradePoints; }
}
