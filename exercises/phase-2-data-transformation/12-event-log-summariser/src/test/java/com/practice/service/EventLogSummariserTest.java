package com.practice.service;

import com.practice.model.AppEvent;
import com.practice.model.EventSummary;
import com.practice.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventLogSummariserTest {

    private EventLogSummariser summariser;

    private final LocalDateTime START = LocalDateTime.of(2024, 1, 1, 0, 0);
    private final LocalDateTime END = LocalDateTime.of(2024, 1, 1, 23, 59, 59);

    @BeforeEach
    void setUp() {
        summariser = new EventLogSummariser();
    }

    private AppEvent event(String id, LocalDateTime ts, EventType type, String user, long duration, boolean success) {
        return new AppEvent(id, ts, type, user, duration, success);
    }

    @Test
    void shouldReturnEmptySummary_whenNoEvents() {
        EventSummary summary = summariser.summarise(List.of(), START, END);
        assertEquals(0, summary.getTotalEvents());
        assertTrue(summary.getTopUsersByActivity().isEmpty());
        assertTrue(summary.getErrorSpikes().isEmpty());
    }

    @Test
    void shouldSetPeriodStartAndEnd_inResult() {
        EventSummary summary = summariser.summarise(List.of(), START, END);
        assertEquals(START, summary.getPeriodStart());
        assertEquals(END, summary.getPeriodEnd());
    }

    @Test
    void shouldExcludeEvents_outsidePeriod() {
        LocalDateTime before = START.minusMinutes(1);
        LocalDateTime after = END.plusMinutes(1);
        List<AppEvent> events = List.of(
            event("E1", before, EventType.LOGIN, "U1", 100, true),
            event("E2", after, EventType.LOGIN, "U1", 100, true)
        );
        EventSummary summary = summariser.summarise(events, START, END);
        assertEquals(0, summary.getTotalEvents());
    }

    @Test
    void shouldIncludeBoundaryEvents_whenAtStartOrEnd() {
        List<AppEvent> events = List.of(
            event("E1", START, EventType.LOGIN, "U1", 100, true),
            event("E2", END, EventType.LOGIN, "U1", 100, true)
        );
        EventSummary summary = summariser.summarise(events, START, END);
        assertEquals(2, summary.getTotalEvents());
    }

    @Test
    void shouldComputeSuccessRate_forUser() {
        List<AppEvent> events = List.of(
            event("E1", START, EventType.LOGIN, "U1", 100, true),
            event("E2", START.plusMinutes(1), EventType.LOGIN, "U1", 100, false),
            event("E3", START.plusMinutes(2), EventType.LOGIN, "U1", 100, true),
            event("E4", START.plusMinutes(3), EventType.LOGIN, "U1", 100, true)
        );
        EventSummary summary = summariser.summarise(events, START, END);
        var userSummary = summary.getTopUsersByActivity().get(0);
        assertEquals(75.0, userSummary.getSuccessRate(), 0.01);
    }

    @Test
    void shouldComputeErrorRate_asRatioNotPercentage() {
        List<AppEvent> events = List.of(
            event("E1", START, EventType.ERROR, "U1", 100, false),
            event("E2", START.plusMinutes(1), EventType.LOGIN, "U1", 100, true),
            event("E3", START.plusMinutes(2), EventType.LOGIN, "U1", 100, true),
            event("E4", START.plusMinutes(3), EventType.LOGIN, "U1", 100, true)
        );
        EventSummary summary = summariser.summarise(events, START, END);
        assertEquals(0.25, summary.getErrorRate(), 0.01);
    }

    @Test
    void shouldLimitTopUsers_toFive() {
        List<AppEvent> events = new ArrayList<>();
        for (int u = 1; u <= 7; u++) {
            for (int e = 0; e < u; e++) {
                events.add(event("E-" + u + "-" + e, START.plusMinutes(u * 10L + e),
                        EventType.LOGIN, "USER-" + String.format("%02d", u), 100, true));
            }
        }
        EventSummary summary = summariser.summarise(events, START, END);
        assertEquals(5, summary.getTopUsersByActivity().size());
    }

    @Test
    void shouldRankTopUsersByEventCountDescending() {
        List<AppEvent> events = List.of(
            event("E1", START, EventType.LOGIN, "U-Alpha", 100, true),
            event("E2", START.plusMinutes(1), EventType.LOGIN, "U-Beta", 100, true),
            event("E3", START.plusMinutes(2), EventType.LOGIN, "U-Beta", 100, true),
            event("E4", START.plusMinutes(3), EventType.LOGIN, "U-Beta", 100, true)
        );
        EventSummary summary = summariser.summarise(events, START, END);
        assertEquals("U-Beta", summary.getTopUsersByActivity().get(0).getUserId());
    }

    @Test
    void shouldDetectErrorSpike_whenErrorsExceedTenPercent() {
        LocalDateTime hour = LocalDateTime.of(2024, 1, 1, 10, 0);
        List<AppEvent> events = new ArrayList<>();
        events.add(event("E1", hour, EventType.ERROR, "U1", 100, false));
        events.add(event("E2", hour.plusMinutes(1), EventType.ERROR, "U1", 100, false));
        for (int i = 0; i < 8; i++) {
            events.add(event("E-ok-" + i, hour.plusMinutes(10 + i), EventType.LOGIN, "U1", 100, true));
        }
        EventSummary summary = summariser.summarise(events, START, END);
        assertTrue(summary.getErrorSpikes().contains("2024-01-01 10:00"));
    }

    @Test
    void shouldNotDetectSpike_whenErrorsAtOrBelowTenPercent() {
        LocalDateTime hour = LocalDateTime.of(2024, 1, 1, 10, 0);
        List<AppEvent> events = new ArrayList<>();
        events.add(event("E1", hour, EventType.ERROR, "U1", 100, false));
        for (int i = 0; i < 9; i++) {
            events.add(event("E-ok-" + i, hour.plusMinutes(1 + i), EventType.LOGIN, "U1", 100, true));
        }
        EventSummary summary = summariser.summarise(events, START, END);
        assertFalse(summary.getErrorSpikes().contains("2024-01-01 10:00"));
    }

    @Test
    void shouldComputeAvgDurationMs_forUser() {
        List<AppEvent> events = List.of(
            event("E1", START, EventType.LOGIN, "U1", 100, true),
            event("E2", START.plusMinutes(1), EventType.LOGIN, "U1", 200, true),
            event("E3", START.plusMinutes(2), EventType.LOGIN, "U1", 300, true)
        );
        EventSummary summary = summariser.summarise(events, START, END);
        assertEquals(200.0, summary.getTopUsersByActivity().get(0).getAvgDurationMs(), 0.01);
    }

    @Test
    void shouldIdentifyMostFrequentEventType_forUser() {
        List<AppEvent> events = List.of(
            event("E1", START, EventType.LOGIN, "U1", 100, true),
            event("E2", START.plusMinutes(1), EventType.SEARCH, "U1", 100, true),
            event("E3", START.plusMinutes(2), EventType.SEARCH, "U1", 100, true),
            event("E4", START.plusMinutes(3), EventType.PAGE_VIEW, "U1", 100, true)
        );
        EventSummary summary = summariser.summarise(events, START, END);
        assertEquals(EventType.SEARCH, summary.getTopUsersByActivity().get(0).getMostFrequentEventType());
    }
}
