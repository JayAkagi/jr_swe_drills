package com.practice.model;

import java.util.List;

public class LoanValidationResult {

    private final boolean approved;
    private final List<Violation> violations;

    private LoanValidationResult(boolean approved, List<Violation> violations) {
        this.approved = approved;
        this.violations = violations;
    }

    public static LoanValidationResult approved() {
        return new LoanValidationResult(true, List.of());
    }

    public static LoanValidationResult rejected(List<Violation> violations) {
        return new LoanValidationResult(false, violations);
    }

    public boolean isApproved() {
        return approved;
    }

    public List<Violation> getViolations() {
        return violations;
    }
}
