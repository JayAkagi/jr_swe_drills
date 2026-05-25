# Exercise 32 — Recurring Event Scheduler

## Scenario

You are building the occurrence-generation engine for a calendar application. Given a recurring event definition with a start date, recurrence type, and interval, generate all occurrences that fall within a given date range.

## Models

| Class | Fields |
|---|---|
| `RecurrenceType` | enum: DAILY, WEEKLY, MONTHLY, YEARLY |
| `RecurringEvent` | name (String), startDate (LocalDate), recurrenceType (RecurrenceType), interval (int), endDate (LocalDate — nullable) |
| `EventOccurrence` | eventName (String), occurrenceDate (LocalDate) |

## Your Task

Implement `RecurringEventScheduler` in `src/main/java/com/practice/service/RecurringEventScheduler.java`:

```java
public List<EventOccurrence> generateOccurrences(RecurringEvent event, LocalDate rangeStart, LocalDate rangeEnd)
```

## Acceptance Criteria

1. DAILY: every `interval` days from `startDate`.
2. WEEKLY: every `interval` weeks (same day of week) from `startDate`.
3. MONTHLY: same day of month every `interval` months; if the day doesn't exist in that month, use the last day of the month.
4. YEARLY: same date every `interval` years from `startDate`.
5. Only occurrences within `[rangeStart, rangeEnd]` inclusive are returned.
6. `event.endDate` is a hard cutoff even if `rangeEnd` is later; `null` endDate means no event-level cutoff.
7. `startDate` itself is included if it falls within the range.

## Run Tests

```bash
mvn test
```

Expected: **14 tests passing**.

## Hint

Start a cursor at `startDate` and advance it by the interval in a loop. In each iteration, check whether the cursor is within both the range and the event's own end date. For MONTHLY, use `startDate.withDayOfMonth(1).plusMonths(n * interval).plusDays(day - 1)` clamped to the month's last day — or more simply, advance with `plusMonths` and use `YearMonth.lengthOfMonth()` to clamp. Stop iterating once the cursor exceeds both `rangeEnd` and `event.endDate`.
