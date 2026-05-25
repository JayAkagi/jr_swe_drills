package com.practice.service;

import com.practice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TravelBookingValidatorTest {

    private TravelBookingValidator validator;
    private static final LocalDate JAN_1 = LocalDate.of(2024, 1, 1);
    private static final LocalDate JAN_5 = LocalDate.of(2024, 1, 5);
    private static final LocalDate JAN_6 = LocalDate.of(2024, 1, 6);
    private static final LocalDate JAN_10 = LocalDate.of(2024, 1, 10);

    @BeforeEach
    void setUp() {
        validator = new TravelBookingValidator();
    }

    // --- Rule 1: FIRST class is not permitted ---

    @Test
    void shouldReject_whenFirstClassIsBooked() {
        TravelRequest request = request("1000", List.of(
                booking(JAN_1, JAN_5, TravelClass.FIRST, "500", BookingType.FLIGHT, false)
        ));
        BookingValidationResult result = validator.validate(request);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "class not permitted"));
    }

    @Test
    void shouldApprove_whenEconomyClassIsBooked() {
        TravelRequest request = request("1000", List.of(
                booking(JAN_1, JAN_5, TravelClass.ECONOMY, "500", BookingType.FLIGHT, false)
        ));
        assertTrue(validator.validate(request).isApproved());
    }

    // --- Rule 2: BUSINESS class requires manager approval ---

    @Test
    void shouldReject_whenBusinessClassWithoutManagerApproval() {
        TravelRequest request = request("1000", List.of(
                booking(JAN_1, JAN_5, TravelClass.BUSINESS, "500", BookingType.FLIGHT, false)
        ));
        BookingValidationResult result = validator.validate(request);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "approval required"));
    }

    @Test
    void shouldApprove_whenBusinessClassWithManagerApproval() {
        TravelRequest request = request("1000", List.of(
                booking(JAN_1, JAN_5, TravelClass.BUSINESS, "500", BookingType.FLIGHT, true)
        ));
        assertTrue(validator.validate(request).isApproved());
    }

    // --- Rule 3: Single booking cannot exceed type limit ---

    @Test
    void shouldReject_whenFlightExceedsTypeLimit() {
        TravelRequest request = request("2000", List.of(
                booking(JAN_1, JAN_5, TravelClass.ECONOMY, "800.01", BookingType.FLIGHT, false)
        ));
        BookingValidationResult result = validator.validate(request);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "limit"));
    }

    @Test
    void shouldApprove_whenFlightIsAtExactTypeLimit() {
        TravelRequest request = request("2000", List.of(
                booking(JAN_1, JAN_5, TravelClass.ECONOMY, "800.00", BookingType.FLIGHT, false)
        ));
        assertTrue(validator.validate(request).isApproved());
    }

    @Test
    void shouldReject_whenHotelExceedsTypeLimit() {
        TravelRequest request = request("2000", List.of(
                booking(JAN_1, JAN_5, TravelClass.ECONOMY, "200.01", BookingType.HOTEL, false)
        ));
        BookingValidationResult result = validator.validate(request);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "limit"));
    }

    @Test
    void shouldReject_whenCarRentalExceedsTypeLimit() {
        TravelRequest request = request("2000", List.of(
                booking(JAN_1, JAN_5, TravelClass.ECONOMY, "150.01", BookingType.CAR_RENTAL, false)
        ));
        BookingValidationResult result = validator.validate(request);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "limit"));
    }

    // --- Rule 4: Total cost cannot exceed budget ---

    @Test
    void shouldReject_whenTotalCostExceedsBudget() {
        TravelRequest request = request("1000", List.of(
                booking(JAN_1, JAN_5, TravelClass.ECONOMY, "700", BookingType.FLIGHT, false),
                booking(JAN_6, JAN_10, TravelClass.ECONOMY, "300.01", BookingType.TRAIN, false)
        ));
        BookingValidationResult result = validator.validate(request);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "budget"));
    }

    @Test
    void shouldApprove_whenTotalCostIsExactlyAtBudget() {
        TravelRequest request = request("1000", List.of(
                booking(JAN_1, JAN_5, TravelClass.ECONOMY, "700", BookingType.FLIGHT, false),
                booking(JAN_6, JAN_10, TravelClass.ECONOMY, "300", BookingType.TRAIN, false)
        ));
        assertTrue(validator.validate(request).isApproved());
    }

    // --- Rule 5: Two bookings of the same type cannot have overlapping dates ---

    @Test
    void shouldReject_whenTwoBookingsOfSameTypeOverlap() {
        LocalDate jan3 = LocalDate.of(2024, 1, 3);
        LocalDate jan8 = LocalDate.of(2024, 1, 8);
        TravelRequest request = request("5000", List.of(
                booking(JAN_1, JAN_5, TravelClass.ECONOMY, "500", BookingType.FLIGHT, false),
                booking(jan3, jan8, TravelClass.ECONOMY, "500", BookingType.FLIGHT, false)
        ));
        BookingValidationResult result = validator.validate(request);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "overlap"));
    }

    @Test
    void shouldApprove_whenTwoBookingsOfSameTypeDoNotOverlap() {
        TravelRequest request = request("5000", List.of(
                booking(JAN_1, JAN_5, TravelClass.ECONOMY, "500", BookingType.FLIGHT, false),
                booking(JAN_6, JAN_10, TravelClass.ECONOMY, "500", BookingType.FLIGHT, false)
        ));
        assertTrue(validator.validate(request).isApproved());
    }

    @Test
    void shouldApprove_whenBookingsOfDifferentTypesOverlap() {
        LocalDate jan3 = LocalDate.of(2024, 1, 3);
        TravelRequest request = request("5000", List.of(
                booking(JAN_1, JAN_10, TravelClass.ECONOMY, "500", BookingType.FLIGHT, false),
                booking(jan3, JAN_10, TravelClass.ECONOMY, "150", BookingType.HOTEL, false)
        ));
        assertTrue(validator.validate(request).isApproved());
    }

    // --- Multiple violations ---

    @Test
    void shouldCollectAllViolations_whenMultipleRulesBroken() {
        TravelRequest request = request("5000", List.of(
                booking(JAN_1, JAN_5, TravelClass.FIRST, "900", BookingType.FLIGHT, false),
                booking(JAN_6, JAN_10, TravelClass.BUSINESS, "850", BookingType.FLIGHT, false)
        ));
        BookingValidationResult result = validator.validate(request);
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "class not permitted"));
        assertTrue(hasViolationContaining(result, "approval required"));
        assertTrue(hasViolationContaining(result, "limit"));
    }

    // --- Happy path ---

    @Test
    void shouldApprove_whenAllRulesPass() {
        TravelRequest request = request("1500", List.of(
                booking(JAN_1, JAN_5, TravelClass.ECONOMY, "600", BookingType.FLIGHT, false),
                booking(JAN_1, JAN_5, TravelClass.ECONOMY, "150", BookingType.HOTEL, false),
                booking(JAN_6, JAN_10, TravelClass.BUSINESS, "300", BookingType.TRAIN, true)
        ));
        BookingValidationResult result = validator.validate(request);
        assertTrue(result.isApproved());
        assertTrue(result.getViolations().isEmpty());
    }

    // --- Helpers ---

    private TravelRequest request(String budget, List<Booking> bookings) {
        return new TravelRequest("Alice", new BigDecimal(budget), bookings);
    }

    private Booking booking(LocalDate dep, LocalDate ret, TravelClass travelClass,
                             String cost, BookingType type, boolean approved) {
        return new Booking("Destination", dep, ret, travelClass, new BigDecimal(cost), type, approved);
    }

    private boolean hasViolationContaining(BookingValidationResult result, String keyword) {
        return result.getViolations().stream()
                .anyMatch(v -> v.getMessage().toLowerCase().contains(keyword.toLowerCase()));
    }
}
