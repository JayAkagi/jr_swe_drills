package com.practice.service;

import com.practice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SLABreachCalculatorTest {

    private SLABreachCalculator calculator;
    private BusinessHours standardHours;

    private static final LocalDateTime MONDAY_9AM = LocalDateTime.of(2024, 1, 8, 9, 0);

    @BeforeEach
    void setUp() {
        calculator = new SLABreachCalculator();
        standardHours = new BusinessHours(9, 17, Set.of(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
    }

    @Test
    void shouldNotBreach_whenTicketResolvedWithinSLA() {
        LocalDateTime resolved = MONDAY_9AM.plusHours(3);
        Ticket ticket = new Ticket("T1", Priority.CRITICAL, MONDAY_9AM, resolved);
        List<SLAResult> results = calculator.evaluate(List.of(ticket), standardHours);
        SLAResult result = results.get(0);
        assertFalse(result.isBreached());
        assertTrue(result.getBreachByHours() < 0);
    }

    @Test
    void shouldBreach_whenTicketResolutionExceedsSLA() {
        LocalDateTime resolved = MONDAY_9AM.plusHours(9);
        Ticket ticket = new Ticket("T2", Priority.CRITICAL, MONDAY_9AM, resolved);
        List<SLAResult> results = calculator.evaluate(List.of(ticket), standardHours);
        SLAResult result = results.get(0);
        assertTrue(result.isBreached());
        assertTrue(result.getBreachByHours() > 0);
    }

    @Test
    void shouldNotCountWeekendHours_whenTicketSpansFridayToMonday() {
        LocalDateTime friday2pm = LocalDateTime.of(2024, 1, 12, 14, 0);
        LocalDateTime monday11am = LocalDateTime.of(2024, 1, 15, 11, 0);
        Ticket ticket = new Ticket("T3", Priority.LOW, friday2pm, monday11am);
        List<SLAResult> results = calculator.evaluate(List.of(ticket), standardHours);
        SLAResult result = results.get(0);
        double expectedHours = 3.0 + 2.0;
        assertEquals(expectedHours, result.getActualBusinessHours(), 0.05);
    }

    @Test
    void shouldNotCountHoursOutsideWorkWindow_whenTicketStartsBeforeWorkday() {
        LocalDateTime start = MONDAY_9AM;
        LocalDateTime end = MONDAY_9AM.plusHours(4);
        Ticket ticket = new Ticket("T4", Priority.HIGH, start, end);
        List<SLAResult> results = calculator.evaluate(List.of(ticket), standardHours);
        SLAResult result = results.get(0);
        assertEquals(4.0, result.getActualBusinessHours(), 0.05);
    }

    @Test
    void shouldHaveCriticalTarget4Hours() {
        Ticket ticket = new Ticket("T5", Priority.CRITICAL, MONDAY_9AM, MONDAY_9AM.plusHours(2));
        List<SLAResult> results = calculator.evaluate(List.of(ticket), standardHours);
        assertEquals(4, results.get(0).getSlaTargetHours());
    }

    @Test
    void shouldHaveHighTarget8Hours() {
        Ticket ticket = new Ticket("T6", Priority.HIGH, MONDAY_9AM, MONDAY_9AM.plusHours(6));
        List<SLAResult> results = calculator.evaluate(List.of(ticket), standardHours);
        assertEquals(8, results.get(0).getSlaTargetHours());
    }

    @Test
    void shouldNotThrow_whenResolvedAtIsNull() {
        Ticket ticket = new Ticket("T7", Priority.MEDIUM, MONDAY_9AM, null);
        assertDoesNotThrow(() -> calculator.evaluate(List.of(ticket), standardHours));
    }

    @Test
    void shouldHaveCorrectBreachByHours_whenTicketIsBreached() {
        LocalDateTime resolved = MONDAY_9AM.plusHours(10);
        Ticket ticket = new Ticket("T8", Priority.CRITICAL, MONDAY_9AM, resolved);
        List<SLAResult> results = calculator.evaluate(List.of(ticket), standardHours);
        SLAResult result = results.get(0);
        assertEquals(6.0, result.getBreachByHours(), 0.05);
    }

    @Test
    void shouldCountMultipleDaysCorrectly_whenTicketSpansWorkingDays() {
        LocalDateTime monday9am = MONDAY_9AM;
        LocalDateTime wednesday9am = LocalDateTime.of(2024, 1, 10, 9, 0);
        Ticket ticket = new Ticket("T9", Priority.LOW, monday9am, wednesday9am);
        List<SLAResult> results = calculator.evaluate(List.of(ticket), standardHours);
        assertEquals(16.0, results.get(0).getActualBusinessHours(), 0.05);
    }

    @Test
    void shouldNotBreach_whenTicketResolvedExactlyAtSLABoundary() {
        LocalDateTime resolved = MONDAY_9AM.plusHours(4);
        Ticket ticket = new Ticket("T10", Priority.CRITICAL, MONDAY_9AM, resolved);
        List<SLAResult> results = calculator.evaluate(List.of(ticket), standardHours);
        assertFalse(results.get(0).isBreached());
    }

    @Test
    void shouldProduceOneResultPerTicket_whenMultipleTicketsEvaluated() {
        Ticket t1 = new Ticket("T11", Priority.HIGH, MONDAY_9AM, MONDAY_9AM.plusHours(5));
        Ticket t2 = new Ticket("T12", Priority.LOW, MONDAY_9AM, MONDAY_9AM.plusHours(10));
        List<SLAResult> results = calculator.evaluate(List.of(t1, t2), standardHours);
        assertEquals(2, results.size());
    }

    @Test
    void shouldHaveMediumTarget24Hours() {
        Ticket ticket = new Ticket("T13", Priority.MEDIUM, MONDAY_9AM, MONDAY_9AM.plusHours(20));
        List<SLAResult> results = calculator.evaluate(List.of(ticket), standardHours);
        assertEquals(24, results.get(0).getSlaTargetHours());
    }
}
