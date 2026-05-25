package com.practice.service;

import com.practice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InsuranceClaimValidatorTest {

    private InsuranceClaimValidator validator;
    private static final LocalDate INCIDENT_DATE = LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setUp() {
        validator = new InsuranceClaimValidator();
    }

    // --- Rule 1: Submission must be within 180 days of incident ---

    @Test
    void shouldApprove_whenSubmittedExactly180DaysAfterIncident() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(180), ClaimType.HOME, "100",
                List.of(item("Laptop", "200", 1, RepairType.REPLACEMENT, true)));
        assertTrue(validator.validate(claim).isApproved());
    }

    @Test
    void shouldReject_whenSubmittedMoreThan180DaysAfterIncident() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(181), ClaimType.HOME, "100",
                List.of(item("Laptop", "200", 1, RepairType.REPLACEMENT, true)));
        ClaimValidationResult result = validator.validate(claim);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "claim too old"));
    }

    // --- Rule 2: Items over £100 require evidence ---

    @Test
    void shouldReject_whenExpensiveItemLacksEvidence() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(30), ClaimType.HOME, "0",
                List.of(item("Camera", "100.01", 1, RepairType.REPLACEMENT, false)));
        ClaimValidationResult result = validator.validate(claim);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "evidence required"));
    }

    @Test
    void shouldApprove_whenExpensiveItemHasEvidence() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(30), ClaimType.HOME, "0",
                List.of(item("Camera", "100.01", 1, RepairType.REPLACEMENT, true)));
        assertTrue(validator.validate(claim).isApproved());
    }

    @Test
    void shouldApprove_whenCheapItemLacksEvidence() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(30), ClaimType.HOME, "0",
                List.of(item("Cable", "100.00", 1, RepairType.REPLACEMENT, false)));
        assertTrue(validator.validate(claim).isApproved());
    }

    // --- Rule 3: Total claim cannot exceed policy limit ---

    @Test
    void shouldReject_whenTotalClaimExceedsPolicyLimit() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(30), ClaimType.TRAVEL, "0",
                List.of(item("Luggage", "5000.01", 1, RepairType.REPLACEMENT, true)));
        ClaimValidationResult result = validator.validate(claim);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "policy limit"));
    }

    @Test
    void shouldApprove_whenTotalClaimIsExactlyAtPolicyLimit() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(30), ClaimType.TRAVEL, "0",
                List.of(item("Luggage", "5000.00", 1, RepairType.REPLACEMENT, true)));
        assertTrue(validator.validate(claim).isApproved());
    }

    // --- Rule 4: Items older than 5 years are only eligible for REPAIR ---

    @Test
    void shouldReject_whenOldItemIsClaimedAsReplacement() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(30), ClaimType.HOME, "0",
                List.of(item("Sofa", "200", 6, RepairType.REPLACEMENT, true)));
        ClaimValidationResult result = validator.validate(claim);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "replacement not eligible"));
    }

    @Test
    void shouldApprove_whenOldItemIsClaimedAsRepair() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(30), ClaimType.HOME, "0",
                List.of(item("Sofa", "200", 6, RepairType.REPAIR, true)));
        assertTrue(validator.validate(claim).isApproved());
    }

    @Test
    void shouldApprove_whenItemIsExactly5YearsOldAndClaimedAsReplacement() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(30), ClaimType.HOME, "0",
                List.of(item("TV", "200", 5, RepairType.REPLACEMENT, true)));
        assertTrue(validator.validate(claim).isApproved());
    }

    // --- Rule 5: Two items with same description and amount are duplicates ---

    @Test
    void shouldReject_whenTwoItemsHaveSameDescriptionAndAmount() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(30), ClaimType.HOME, "0",
                List.of(
                        item("Watch", "150", 2, RepairType.REPLACEMENT, true),
                        item("Watch", "150", 3, RepairType.REPLACEMENT, true)
                ));
        ClaimValidationResult result = validator.validate(claim);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "duplicate"));
    }

    @Test
    void shouldApprove_whenItemsHaveSameDescriptionButDifferentAmount() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(30), ClaimType.HOME, "0",
                List.of(
                        item("Watch", "150", 2, RepairType.REPLACEMENT, true),
                        item("Watch", "200", 2, RepairType.REPLACEMENT, true)
                ));
        assertTrue(validator.validate(claim).isApproved());
    }

    // --- Rule 6: Net claim (total - excess) must be greater than zero ---

    @Test
    void shouldReject_whenNetClaimIsZero() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(30), ClaimType.HOME, "200",
                List.of(item("Phone", "200", 1, RepairType.REPLACEMENT, true)));
        ClaimValidationResult result = validator.validate(claim);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "below excess"));
    }

    @Test
    void shouldApprove_whenNetClaimIsPositive() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(30), ClaimType.HOME, "100",
                List.of(item("Phone", "200", 1, RepairType.REPLACEMENT, true)));
        assertTrue(validator.validate(claim).isApproved());
    }

    // --- Multiple violations ---

    @Test
    void shouldCollectAllViolations_whenMultipleRulesBroken() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(181), ClaimType.TRAVEL, "0",
                List.of(
                        item("Camera", "200", 1, RepairType.REPLACEMENT, false),
                        item("Camera", "200", 1, RepairType.REPLACEMENT, false)
                ));
        ClaimValidationResult result = validator.validate(claim);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "claim too old"));
        assertTrue(hasViolationContaining(result, "evidence required"));
        assertTrue(hasViolationContaining(result, "duplicate"));
    }

    // --- Happy path ---

    @Test
    void shouldApprove_whenAllRulesPass() {
        Claim claim = claim(INCIDENT_DATE, INCIDENT_DATE.plusDays(90), ClaimType.HOME, "100",
                List.of(
                        item("Laptop", "500", 2, RepairType.REPLACEMENT, true),
                        item("Phone", "50", 1, RepairType.REPLACEMENT, false)
                ));
        ClaimValidationResult result = validator.validate(claim);
        assertTrue(result.isApproved());
        assertTrue(result.getViolations().isEmpty());
    }

    // --- Helpers ---

    private Claim claim(LocalDate incident, LocalDate submission, ClaimType type,
                        String excess, List<ClaimItem> items) {
        return new Claim("POL-001", "Alice", incident, submission, type, items, new BigDecimal(excess));
    }

    private ClaimItem item(String description, String amount, int ageYears,
                           RepairType repairType, boolean evidence) {
        return new ClaimItem(description, new BigDecimal(amount), ageYears, repairType, evidence);
    }

    private boolean hasViolationContaining(ClaimValidationResult result, String keyword) {
        return result.getViolations().stream()
                .anyMatch(v -> v.getMessage().toLowerCase().contains(keyword.toLowerCase()));
    }
}
