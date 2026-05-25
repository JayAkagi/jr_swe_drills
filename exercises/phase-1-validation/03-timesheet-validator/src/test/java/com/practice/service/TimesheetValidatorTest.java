package com.practice.service;

import com.practice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimesheetValidatorTest {

    private TimesheetValidator validator;
    private static final LocalDate WEEK_START = LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setUp() {
        validator = new TimesheetValidator();
    }

    // --- Rule 1: Total hours per day cannot exceed 12 ---

    @Test
    void shouldApprove_whenDayHasExactly12Hours() {
        List<TimeEntry> entries = List.of(
                entry(WEEK_START, 8.0, "PROJ-A", EntryType.REGULAR),
                entry(WEEK_START, 4.0, "PROJ-B", EntryType.OVERTIME)
        );
        assertTrue(validator.validate(timesheet(WEEK_START, WEEK_START.plusDays(7), entries)).isApproved());
    }

    @Test
    void shouldReject_whenDayExceeds12Hours() {
        List<TimeEntry> entries = List.of(
                entry(WEEK_START, 8.0, "PROJ-A", EntryType.REGULAR),
                entry(WEEK_START, 5.0, "PROJ-B", EntryType.OVERTIME)
        );
        TimesheetValidationResult result = validator.validate(timesheet(WEEK_START, WEEK_START.plusDays(7), entries));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "daily hours"));
    }

    // --- Rule 2: OVERTIME on a day requires REGULAR hours >= 8 that day ---

    @Test
    void shouldApprove_whenOvertimeExistsWithExactly8RegularHours() {
        List<TimeEntry> entries = List.of(
                entry(WEEK_START, 8.0, "PROJ-A", EntryType.REGULAR),
                entry(WEEK_START, 2.0, "PROJ-B", EntryType.OVERTIME)
        );
        assertTrue(validator.validate(timesheet(WEEK_START, WEEK_START.plusDays(7), entries)).isApproved());
    }

    @Test
    void shouldReject_whenOvertimeExistsWithLessThan8RegularHours() {
        List<TimeEntry> entries = List.of(
                entry(WEEK_START, 6.0, "PROJ-A", EntryType.REGULAR),
                entry(WEEK_START, 2.0, "PROJ-B", EntryType.OVERTIME)
        );
        TimesheetValidationResult result = validator.validate(timesheet(WEEK_START, WEEK_START.plusDays(7), entries));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "overtime"));
    }

    @Test
    void shouldReject_whenOvertimeExistsWithNoRegularHours() {
        List<TimeEntry> entries = List.of(
                entry(WEEK_START, 4.0, "PROJ-A", EntryType.OVERTIME)
        );
        TimesheetValidationResult result = validator.validate(timesheet(WEEK_START, WEEK_START.plusDays(7), entries));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "overtime"));
    }

    // --- Rule 3: LEAVE entries must be exactly 8 hours ---

    @Test
    void shouldApprove_whenLeaveEntryIsExactly8Hours() {
        List<TimeEntry> entries = List.of(
                entry(WEEK_START, 8.0, "LEAVE", EntryType.LEAVE)
        );
        assertTrue(validator.validate(timesheet(WEEK_START, WEEK_START.plusDays(7), entries)).isApproved());
    }

    @Test
    void shouldReject_whenLeaveEntryIsNot8Hours() {
        List<TimeEntry> entries = List.of(
                entry(WEEK_START, 4.0, "LEAVE", EntryType.LEAVE)
        );
        TimesheetValidationResult result = validator.validate(timesheet(WEEK_START, WEEK_START.plusDays(7), entries));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "leave hours"));
    }

    // --- Rule 4: Submission must be within 7 days of week end date ---

    @Test
    void shouldApprove_whenSubmittedExactly7DaysAfterWeekEnd() {
        LocalDate weekEnd = WEEK_START.plusDays(6);
        List<TimeEntry> entries = List.of(entry(WEEK_START, 8.0, "PROJ-A", EntryType.REGULAR));
        assertTrue(validator.validate(timesheet(WEEK_START, weekEnd.plusDays(7), entries)).isApproved());
    }

    @Test
    void shouldReject_whenSubmittedMoreThan7DaysAfterWeekEnd() {
        LocalDate weekEnd = WEEK_START.plusDays(6);
        List<TimeEntry> entries = List.of(entry(WEEK_START, 8.0, "PROJ-A", EntryType.REGULAR));
        TimesheetValidationResult result = validator.validate(timesheet(WEEK_START, weekEnd.plusDays(8), entries));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "late submission"));
    }

    // --- Rule 5: Same project code cannot appear twice on the same day ---

    @Test
    void shouldReject_whenSameProjectCodeAppearsOnSameDay() {
        List<TimeEntry> entries = List.of(
                entry(WEEK_START, 4.0, "PROJ-A", EntryType.REGULAR),
                entry(WEEK_START, 4.0, "PROJ-A", EntryType.REGULAR)
        );
        TimesheetValidationResult result = validator.validate(timesheet(WEEK_START, WEEK_START.plusDays(7), entries));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "duplicate project"));
    }

    @Test
    void shouldApprove_whenSameProjectCodeAppearsOnDifferentDays() {
        List<TimeEntry> entries = List.of(
                entry(WEEK_START, 8.0, "PROJ-A", EntryType.REGULAR),
                entry(WEEK_START.plusDays(1), 8.0, "PROJ-A", EntryType.REGULAR)
        );
        assertTrue(validator.validate(timesheet(WEEK_START, WEEK_START.plusDays(7), entries)).isApproved());
    }

    // --- Multiple violations ---

    @Test
    void shouldCollectAllViolations_whenMultipleRulesBroken() {
        LocalDate weekEnd = WEEK_START.plusDays(6);
        List<TimeEntry> entries = List.of(
                entry(WEEK_START, 4.0, "LEAVE", EntryType.LEAVE),
                entry(WEEK_START.plusDays(1), 4.0, "PROJ-A", EntryType.OVERTIME)
        );
        TimesheetValidationResult result = validator.validate(timesheet(WEEK_START, weekEnd.plusDays(8), entries));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "leave hours"));
        assertTrue(hasViolationContaining(result, "overtime"));
        assertTrue(hasViolationContaining(result, "late submission"));
    }

    // --- Happy path ---

    @Test
    void shouldApprove_whenAllRulesPass() {
        List<TimeEntry> entries = List.of(
                entry(WEEK_START, 8.0, "PROJ-A", EntryType.REGULAR),
                entry(WEEK_START.plusDays(1), 8.0, "LEAVE", EntryType.LEAVE),
                entry(WEEK_START.plusDays(2), 8.0, "PROJ-B", EntryType.REGULAR),
                entry(WEEK_START.plusDays(2), 2.0, "PROJ-C", EntryType.OVERTIME)
        );
        TimesheetValidationResult result = validator.validate(timesheet(WEEK_START, WEEK_START.plusDays(7), entries));
        assertTrue(result.isApproved());
        assertTrue(result.getViolations().isEmpty());
    }

    // --- Helpers ---

    private TimeEntry entry(LocalDate date, double hours, String projectCode, EntryType type) {
        return new TimeEntry(date, hours, projectCode, type);
    }

    private Timesheet timesheet(LocalDate weekStart, LocalDate submissionDate, List<TimeEntry> entries) {
        return new Timesheet("John Smith", weekStart, submissionDate, entries);
    }

    private boolean hasViolationContaining(TimesheetValidationResult result, String keyword) {
        return result.getViolations().stream()
                .anyMatch(v -> v.getMessage().toLowerCase().contains(keyword.toLowerCase()));
    }
}
