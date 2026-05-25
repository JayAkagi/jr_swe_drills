# Exercise 17 — SLA Breach Calculator

## Scenario

A support platform needs to track whether tickets were resolved within their Service Level Agreement. Resolution time is measured in business hours only — time outside working hours and on non-working days does not count. Unresolved tickets are evaluated against the current time.

## Models

| Class | Key Fields |
|-------|-----------|
| `Priority` | enum: LOW, MEDIUM, HIGH, CRITICAL |
| `Ticket` | ticketId, priority, createdAt, resolvedAt (nullable) |
| `BusinessHours` | startHour, endHour, workingDays (Set\<DayOfWeek\>) |
| `SLAResult` | ticketId, priority, slaTargetHours, actualBusinessHours, breached, breachByHours |

## Method Signature

```java
public List<SLAResult> evaluate(List<Ticket> tickets, BusinessHours hours)
```

## Criteria

1. SLA targets (business hours): CRITICAL = 4, HIGH = 8, MEDIUM = 24, LOW = 72.
2. Only minutes that fall on a working day AND within `[startHour, endHour)` count toward elapsed business time.
3. For each qualifying minute, increment a counter. Divide by 60.0 to get `actualBusinessHours`.
4. If `resolvedAt` is null, use `LocalDateTime.now()` as the end time.
5. `breachByHours = actualBusinessHours - slaTargetHours` (negative means resolved early).
6. `breached = actualBusinessHours > slaTargetHours`.
7. Produce one `SLAResult` per ticket.

## Running Tests

```bash
cd 17-sla-breach-calculator
mvn test
```

<details>
<summary>Hint</summary>

Iterate minute by minute from `createdAt` to the end time. For each minute, check `hours.getWorkingDays().contains(dayOfWeek)` and `minute.getHour() >= startHour && minute.getHour() < endHour`. Count qualifying minutes and divide by 60.0. This is simple but correct — performance is acceptable for test data spanning days or weeks.

</details>
