package com.practice.model;

import java.util.List;

public class BookingValidationResult {

    private final boolean approved;
    private final List<Violation> violations;

    private BookingValidationResult(boolean approved, List<Violation> violations) {
        this.approved = approved;
        this.violations = violations;
    }

    public static BookingValidationResult approved() {
        return new BookingValidationResult(true, List.of());
    }

    public static BookingValidationResult rejected(List<Violation> violations) {
        return new BookingValidationResult(false, violations);
    }

    public boolean isApproved() { return approved; }
    public List<Violation> getViolations() { return violations; }
}
