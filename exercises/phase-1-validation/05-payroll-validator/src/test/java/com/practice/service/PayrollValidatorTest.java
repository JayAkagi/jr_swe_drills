package com.practice.service;

import com.practice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PayrollValidatorTest {

    private PayrollValidator validator;
    private static final YearMonth JAN_2024 = YearMonth.of(2024, 1);
    private static final LocalDate JAN_31 = LocalDate.of(2024, 1, 31);

    @BeforeEach
    void setUp() {
        validator = new PayrollValidator();
    }

    // --- Rule 1: Basic salary cannot exceed department cap ---

    @Test
    void shouldReject_whenEngineeringSalaryExceedsCap() {
        PayrollSubmission submission = submission(JAN_31, List.of(
                entry("EMP001", Department.ENGINEERING, "8000.01", "0", "0", JAN_2024)
        ));
        PayrollValidationResult result = validator.validate(submission);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "salary cap"));
    }

    @Test
    void shouldApprove_whenSalaryIsExactlyAtDepartmentCap() {
        PayrollSubmission submission = submission(JAN_31, List.of(
                entry("EMP001", Department.ENGINEERING, "8000", "0", "0", JAN_2024)
        ));
        assertTrue(validator.validate(submission).isApproved());
    }

    @Test
    void shouldReject_whenHrSalaryExceedsCap() {
        PayrollSubmission submission = submission(JAN_31, List.of(
                entry("EMP001", Department.HR, "5500.01", "0", "0", JAN_2024)
        ));
        PayrollValidationResult result = validator.validate(submission);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "salary cap"));
    }

    // --- Rule 2: Bonus cannot exceed 30% of basic salary ---

    @Test
    void shouldReject_whenBonusExceeds30PercentOfBasicSalary() {
        PayrollSubmission submission = submission(JAN_31, List.of(
                entry("EMP001", Department.ENGINEERING, "5000", "1500.01", "0", JAN_2024)
        ));
        PayrollValidationResult result = validator.validate(submission);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "bonus limit"));
    }

    @Test
    void shouldApprove_whenBonusIsExactly30PercentOfBasicSalary() {
        PayrollSubmission submission = submission(JAN_31, List.of(
                entry("EMP001", Department.ENGINEERING, "5000", "1500", "0", JAN_2024)
        ));
        assertTrue(validator.validate(submission).isApproved());
    }

    // --- Rule 3: Deductions cannot exceed 40% of basic salary ---

    @Test
    void shouldReject_whenDeductionsExceed40PercentOfBasicSalary() {
        PayrollSubmission submission = submission(JAN_31, List.of(
                entry("EMP001", Department.ENGINEERING, "5000", "0", "2000.01", JAN_2024)
        ));
        PayrollValidationResult result = validator.validate(submission);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "deductions"));
    }

    @Test
    void shouldApprove_whenDeductionsAreExactly40PercentOfBasicSalary() {
        PayrollSubmission submission = submission(JAN_31, List.of(
                entry("EMP001", Department.ENGINEERING, "5000", "0", "2000", JAN_2024)
        ));
        assertTrue(validator.validate(submission).isApproved());
    }

    // --- Rule 4: Same employeeId cannot appear twice ---

    @Test
    void shouldReject_whenSameEmployeeIdAppearsTwice() {
        PayrollSubmission submission = submission(JAN_31, List.of(
                entry("EMP001", Department.ENGINEERING, "5000", "0", "0", JAN_2024),
                entry("EMP001", Department.SALES, "4000", "0", "0", JAN_2024)
        ));
        PayrollValidationResult result = validator.validate(submission);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "duplicate employee"));
    }

    @Test
    void shouldApprove_whenAllEmployeeIdsAreUnique() {
        PayrollSubmission submission = submission(JAN_31, List.of(
                entry("EMP001", Department.ENGINEERING, "5000", "0", "0", JAN_2024),
                entry("EMP002", Department.SALES, "4000", "0", "0", JAN_2024)
        ));
        assertTrue(validator.validate(submission).isApproved());
    }

    // --- Rule 5: Submission date must fall within the same calendar month as payPeriod ---

    @Test
    void shouldReject_whenSubmissionDateIsOutsidePayPeriodMonth() {
        LocalDate feb1 = LocalDate.of(2024, 2, 1);
        PayrollSubmission submission = submission(feb1, List.of(
                entry("EMP001", Department.ENGINEERING, "5000", "0", "0", JAN_2024)
        ));
        PayrollValidationResult result = validator.validate(submission);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "pay period"));
    }

    @Test
    void shouldApprove_whenSubmissionDateIsWithinPayPeriodMonth() {
        PayrollSubmission submission = submission(LocalDate.of(2024, 1, 15), List.of(
                entry("EMP001", Department.ENGINEERING, "5000", "0", "0", JAN_2024)
        ));
        assertTrue(validator.validate(submission).isApproved());
    }

    // --- Rule 6: Total payroll cannot exceed £500,000 ---

    @Test
    void shouldReject_whenTotalPayrollExceeds500000() {
        List<PayrollEntry> entries = new ArrayList<>();
        for (int i = 1; i <= 63; i++) {
            entries.add(new PayrollEntry(
                    String.format("EMP%03d", i), "Employee " + i,
                    Department.ENGINEERING,
                    new BigDecimal("8000"), BigDecimal.ZERO, BigDecimal.ZERO,
                    JAN_2024
            ));
        }
        PayrollValidationResult result = validator.validate(new PayrollSubmission("admin", JAN_31, entries));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "total payroll"));
    }

    @Test
    void shouldApprove_whenTotalPayrollIsWithinLimit() {
        List<PayrollEntry> entries = new ArrayList<>();
        for (int i = 1; i <= 62; i++) {
            entries.add(new PayrollEntry(
                    String.format("EMP%03d", i), "Employee " + i,
                    Department.ENGINEERING,
                    new BigDecimal("8000"), BigDecimal.ZERO, BigDecimal.ZERO,
                    JAN_2024
            ));
        }
        assertTrue(validator.validate(new PayrollSubmission("admin", JAN_31, entries)).isApproved());
    }

    // --- Multiple violations ---

    @Test
    void shouldCollectAllViolations_whenMultipleRulesBroken() {
        PayrollSubmission submission = submission(JAN_31, List.of(
                entry("EMP001", Department.ENGINEERING, "9000", "0", "0", JAN_2024),
                entry("EMP001", Department.SALES, "5000", "2000", "0", JAN_2024)
        ));
        PayrollValidationResult result = validator.validate(submission);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "salary cap"));
        assertTrue(hasViolationContaining(result, "bonus limit"));
        assertTrue(hasViolationContaining(result, "duplicate employee"));
    }

    // --- Happy path ---

    @Test
    void shouldApprove_whenAllRulesPass() {
        PayrollSubmission submission = submission(JAN_31, List.of(
                entry("EMP001", Department.ENGINEERING, "6000", "1200", "1000", JAN_2024),
                entry("EMP002", Department.SALES, "5000", "500", "500", JAN_2024),
                entry("EMP003", Department.HR, "4000", "0", "0", JAN_2024)
        ));
        PayrollValidationResult result = validator.validate(submission);
        assertTrue(result.isApproved());
        assertTrue(result.getViolations().isEmpty());
    }

    // --- Helpers ---

    private PayrollSubmission submission(LocalDate submissionDate, List<PayrollEntry> entries) {
        return new PayrollSubmission("payroll-admin", submissionDate, entries);
    }

    private PayrollEntry entry(String id, Department dept, String salary, String bonus, String deductions, YearMonth period) {
        return new PayrollEntry(id, "Employee " + id, dept,
                new BigDecimal(salary), new BigDecimal(bonus), new BigDecimal(deductions), period);
    }

    private boolean hasViolationContaining(PayrollValidationResult result, String keyword) {
        return result.getViolations().stream()
                .anyMatch(v -> v.getMessage().toLowerCase().contains(keyword.toLowerCase()));
    }
}
