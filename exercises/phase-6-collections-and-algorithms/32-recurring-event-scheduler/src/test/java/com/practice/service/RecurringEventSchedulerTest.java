package com.practice.service;

import com.practice.model.EventOccurrence;
import com.practice.model.RecurrenceType;
import com.practice.model.RecurringEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecurringEventSchedulerTest {

    private RecurringEventScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new RecurringEventScheduler();
    }

    @Test
    void shouldGenerateDailyOccurrences_everyOneDay() {
        RecurringEvent event = new RecurringEvent("Standup", LocalDate.of(2024, 1, 1), RecurrenceType.DAILY, 1, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5));
        assertEquals(5, occurrences.size());
        assertEquals(LocalDate.of(2024, 1, 1), occurrences.get(0).getOccurrenceDate());
        assertEquals(LocalDate.of(2024, 1, 5), occurrences.get(4).getOccurrenceDate());
    }

    @Test
    void shouldGenerateDailyOccurrences_everyTwoDays() {
        RecurringEvent event = new RecurringEvent("Exercise", LocalDate.of(2024, 1, 1), RecurrenceType.DAILY, 2, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 7));
        assertEquals(4, occurrences.size());
        assertEquals(LocalDate.of(2024, 1, 1), occurrences.get(0).getOccurrenceDate());
        assertEquals(LocalDate.of(2024, 1, 3), occurrences.get(1).getOccurrenceDate());
        assertEquals(LocalDate.of(2024, 1, 5), occurrences.get(2).getOccurrenceDate());
        assertEquals(LocalDate.of(2024, 1, 7), occurrences.get(3).getOccurrenceDate());
    }

    @Test
    void shouldGenerateWeeklyOccurrences_everyOneWeek() {
        RecurringEvent event = new RecurringEvent("Review", LocalDate.of(2024, 1, 1), RecurrenceType.WEEKLY, 1, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 29));
        assertEquals(5, occurrences.size());
        assertEquals(LocalDate.of(2024, 1, 8), occurrences.get(1).getOccurrenceDate());
        assertEquals(LocalDate.of(2024, 1, 15), occurrences.get(2).getOccurrenceDate());
    }

    @Test
    void shouldGenerateWeeklyOccurrences_everyTwoWeeks() {
        RecurringEvent event = new RecurringEvent("Sync", LocalDate.of(2024, 1, 1), RecurrenceType.WEEKLY, 2, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 2, 12));
        assertEquals(3, occurrences.size());
        assertEquals(LocalDate.of(2024, 1, 1), occurrences.get(0).getOccurrenceDate());
        assertEquals(LocalDate.of(2024, 1, 15), occurrences.get(1).getOccurrenceDate());
        assertEquals(LocalDate.of(2024, 1, 29), occurrences.get(2).getOccurrenceDate());
    }

    @Test
    void shouldGenerateMonthlyOccurrences_everyOneMonth() {
        RecurringEvent event = new RecurringEvent("Report", LocalDate.of(2024, 1, 15), RecurrenceType.MONTHLY, 1, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 4, 30));
        assertEquals(4, occurrences.size());
        assertEquals(LocalDate.of(2024, 1, 15), occurrences.get(0).getOccurrenceDate());
        assertEquals(LocalDate.of(2024, 2, 15), occurrences.get(1).getOccurrenceDate());
        assertEquals(LocalDate.of(2024, 3, 15), occurrences.get(2).getOccurrenceDate());
        assertEquals(LocalDate.of(2024, 4, 15), occurrences.get(3).getOccurrenceDate());
    }

    @Test
    void shouldUseLastDayOfMonth_whenDayDoesNotExist() {
        RecurringEvent event = new RecurringEvent("Payroll", LocalDate.of(2024, 1, 31), RecurrenceType.MONTHLY, 1, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 31));
        assertEquals(3, occurrences.size());
        assertEquals(LocalDate.of(2024, 1, 31), occurrences.get(0).getOccurrenceDate());
        assertEquals(LocalDate.of(2024, 2, 29), occurrences.get(1).getOccurrenceDate());
        assertEquals(LocalDate.of(2024, 3, 31), occurrences.get(2).getOccurrenceDate());
    }

    @Test
    void shouldGenerateYearlyOccurrences_everyOneYear() {
        RecurringEvent event = new RecurringEvent("Anniversary", LocalDate.of(2020, 6, 15), RecurrenceType.YEARLY, 1, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2020, 1, 1), LocalDate.of(2023, 12, 31));
        assertEquals(4, occurrences.size());
        assertEquals(LocalDate.of(2020, 6, 15), occurrences.get(0).getOccurrenceDate());
        assertEquals(LocalDate.of(2023, 6, 15), occurrences.get(3).getOccurrenceDate());
    }

    @Test
    void shouldIncludeStartDate_whenWithinRange() {
        RecurringEvent event = new RecurringEvent("Meeting", LocalDate.of(2024, 3, 1), RecurrenceType.DAILY, 1, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 3));
        assertEquals(LocalDate.of(2024, 3, 1), occurrences.get(0).getOccurrenceDate());
    }

    @Test
    void shouldExcludeStartDate_whenBeforeRangeStart() {
        RecurringEvent event = new RecurringEvent("Meeting", LocalDate.of(2024, 1, 1), RecurrenceType.DAILY, 1, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 1, 3), LocalDate.of(2024, 1, 5));
        assertEquals(LocalDate.of(2024, 1, 3), occurrences.get(0).getOccurrenceDate());
        assertEquals(3, occurrences.size());
    }

    @Test
    void shouldApplyEventEndDate_asHardCutoff() {
        RecurringEvent event = new RecurringEvent("Task", LocalDate.of(2024, 1, 1), RecurrenceType.DAILY, 1, LocalDate.of(2024, 1, 3));
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10));
        assertEquals(3, occurrences.size());
        assertEquals(LocalDate.of(2024, 1, 3), occurrences.get(2).getOccurrenceDate());
    }

    @Test
    void shouldApplyRangeEnd_asInclusiveCutoff() {
        RecurringEvent event = new RecurringEvent("Task", LocalDate.of(2024, 1, 1), RecurrenceType.DAILY, 1, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 4));
        assertEquals(4, occurrences.size());
        assertEquals(LocalDate.of(2024, 1, 4), occurrences.get(3).getOccurrenceDate());
    }

    @Test
    void shouldProduceOccurrences_whenNullEndDate() {
        RecurringEvent event = new RecurringEvent("Daily", LocalDate.of(2024, 5, 1), RecurrenceType.DAILY, 1, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 10));
        assertEquals(10, occurrences.size());
    }

    @Test
    void shouldSetEventName_onEachOccurrence() {
        RecurringEvent event = new RecurringEvent("MyEvent", LocalDate.of(2024, 1, 1), RecurrenceType.DAILY, 1, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 3));
        assertTrue(occurrences.stream().allMatch(o -> o.getEventName().equals("MyEvent")));
    }

    @Test
    void shouldReturnCorrectCount_forWeeklyIntervalOne() {
        RecurringEvent event = new RecurringEvent("Weekly", LocalDate.of(2024, 1, 1), RecurrenceType.WEEKLY, 1, null);
        List<EventOccurrence> occurrences = scheduler.generateOccurrences(event, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 25));
        assertEquals(13, occurrences.size());
    }
}
