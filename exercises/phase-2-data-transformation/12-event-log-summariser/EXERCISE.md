# Exercise 12 — Event Log Summariser

## Your Task
Implement the `summarise()` method inside:
```
src/main/java/com/practice/service/EventLogSummariser.java
```
Do not modify any other file.

---

## Scenario
You receive a stream of application events and need to produce a summary report for a specified time period. The summary covers overall metrics, per-user activity, and identifies hours where errors spiked above a threshold.

---

## The Models (already built for you)

**`EventType`** enum — `LOGIN`, `PURCHASE`, `SEARCH`, `PAGE_VIEW`, `ERROR`

**`AppEvent`** — a single application event
| Field | Type | Description |
|---|---|---|
| eventId | String | Unique event ID |
| timestamp | LocalDateTime | When the event occurred |
| eventType | EventType | Type of event |
| userId | String | User who triggered the event |
| durationMs | long | Duration of the event in milliseconds |
| success | boolean | Whether the event succeeded |

**`UserSummary`** — per-user aggregated metrics
| Field | Type | Description |
|---|---|---|
| userId | String | User ID |
| totalEvents | int | Total events in the period for this user |
| successRate | double | (successful / total) × 100, rounded to 2dp |
| avgDurationMs | double | Average event duration, rounded to 2dp |
| mostFrequentEventType | EventType | The event type with the highest count for this user |

**`EventSummary`** — the complete period summary
| Field | Type | Description |
|---|---|---|
| periodStart | LocalDateTime | As provided |
| periodEnd | LocalDateTime | As provided |
| totalEvents | int | Total events in the period |
| errorRate | double | ERROR count / totalEvents (ratio 0–1), rounded to 2dp |
| topUsersByActivity | List\<UserSummary\> | Top 5 users by event count desc; ties broken by userId asc |
| errorSpikes | List\<String\> | Hours formatted "yyyy-MM-dd HH:00" where ERROR count > 10% of that hour's events |

---

## Method to Implement
`EventSummary summarise(List<AppEvent> events, LocalDateTime periodStart, LocalDateTime periodEnd)`

---

## Criteria
1. Only events whose timestamp falls within [periodStart, periodEnd] inclusive are included.
2. `successRate` per user = (number of successful events / totalEvents) × 100, rounded to 2 decimal places.
3. `errorRate` = number of ERROR events / totalEvents (as a ratio, not percentage), rounded to 2 decimal places.
4. `topUsersByActivity` = top 5 users by total event count descending; ties broken by userId ascending.
5. `errorSpikes` = hours (truncated to the hour) where ERROR events exceed 10% of all events in that hour, formatted as "yyyy-MM-dd HH:00".
6. `avgDurationMs` per user = average of `durationMs` across all events for that user, rounded to 2 decimal places.

---

## Running the Tests
```bash
mvn test
```
There are 12 tests. They all fail until your implementation is correct.
You are done when all 12 pass.

---

## Hint (read only if stuck)
<details>
<summary>Click to reveal</summary>
Filter events to the period first. Group by userId for user summaries. For errorSpikes, group by timestamp.truncatedTo(ChronoUnit.HOURS), then check if ERROR count / hourTotal > 0.10. For topUsers, sort by totalEvents descending then userId ascending and take the first 5. Use a DateTimeFormatter for the spike hour label.
</details>
