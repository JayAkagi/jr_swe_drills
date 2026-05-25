package com.practice.model;

public class ModuleResult {
    private final String moduleCode;
    private final String moduleName;
    private final int credits;
    private final double scorePercent;
    private final int semester;

    public ModuleResult(String moduleCode, String moduleName, int credits, double scorePercent, int semester) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.credits = credits;
        this.scorePercent = scorePercent;
        this.semester = semester;
    }

    public String getModuleCode() { return moduleCode; }
    public String getModuleName() { return moduleName; }
    public int getCredits() { return credits; }
    public double getScorePercent() { return scorePercent; }
    public int getSemester() { return semester; }
}
