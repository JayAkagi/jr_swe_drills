package com.practice.model;

import java.util.List;

public class InvoiceValidationResult {

    private final boolean approved;
    private final List<Violation> violations;

    private InvoiceValidationResult(boolean approved, List<Violation> violations) {
        this.approved = approved;
        this.violations = violations;
    }

    public static InvoiceValidationResult approved() {
        return new InvoiceValidationResult(true, List.of());
    }

    public static InvoiceValidationResult rejected(List<Violation> violations) {
        return new InvoiceValidationResult(false, violations);
    }

    public boolean isApproved() {
        return approved;
    }

    public List<Violation> getViolations() {
        return violations;
    }
}
