package com.practice.model;

import java.util.List;

public class TimesheetValidationResult {

    private final boolean approved;
    private final List<Violation> violations;

    private TimesheetValidationResult(boolean approved, List<Violation> violations) {
        this.approved = approved;
        this.violations = violations;
    }

    public static TimesheetValidationResult approved() {
        return new TimesheetValidationResult(true, List.of());
    }

    public static TimesheetValidationResult rejected(List<Violation> violations) {
        return new TimesheetValidationResult(false, violations);
    }

    public boolean isApproved() { return approved; }
    public List<Violation> getViolations() { return violations; }
}
