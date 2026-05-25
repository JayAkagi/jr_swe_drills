package com.practice.model;

import java.util.List;

public class ClaimValidationResult {

    private final boolean approved;
    private final List<Violation> violations;

    private ClaimValidationResult(boolean approved, List<Violation> violations) {
        this.approved = approved;
        this.violations = violations;
    }

    public static ClaimValidationResult approved() {
        return new ClaimValidationResult(true, List.of());
    }

    public static ClaimValidationResult rejected(List<Violation> violations) {
        return new ClaimValidationResult(false, violations);
    }

    public boolean isApproved() { return approved; }
    public List<Violation> getViolations() { return violations; }
}
