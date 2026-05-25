package com.practice.model;

import java.util.List;

public class PayrollValidationResult {

    private final boolean approved;
    private final List<Violation> violations;

    private PayrollValidationResult(boolean approved, List<Violation> violations) {
        this.approved = approved;
        this.violations = violations;
    }

    public static PayrollValidationResult approved() {
        return new PayrollValidationResult(true, List.of());
    }

    public static PayrollValidationResult rejected(List<Violation> violations) {
        return new PayrollValidationResult(false, violations);
    }

    public boolean isApproved() { return approved; }
    public List<Violation> getViolations() { return violations; }
}
