# Exercise 29 — Top-N Leaderboard

## Scenario

You are building a leaderboard service for an online gaming platform. Given a list of score entries, produce a ranked top-N leaderboard with tie-breaking rules and standard competitive ranking (1, 1, 3 — not 1, 2, 3 when tied).

## Models

| Class | Fields |
|---|---|
| `ScoreEntry` | playerId, playerName, score (int), completionTimeSeconds (int), achievementDate (LocalDate) |
| `LeaderboardEntry` | rank (int), playerId, playerName, score (int), completionTimeSeconds (int) |
| `Leaderboard` | gameName (String), generatedAt (LocalDateTime), entries (List\<LeaderboardEntry\>) |

## Your Task

Implement `LeaderboardGenerator` in `src/main/java/com/practice/service/LeaderboardGenerator.java`:

```java
public Leaderboard generate(String gameName, List<ScoreEntry> entries, int topN)
```

## Acceptance Criteria

1. Ranked by score descending.
2. Tie on score: faster `completionTimeSeconds` wins (ascending).
3. Further tie: earlier `achievementDate` wins (ascending).
4. Ranks are 1-indexed; tied scores share rank; the next rank skips (e.g., two entries at rank 2 means the next rank is 4).
5. Only top N entries are included in the result.

## Run Tests

```bash
mvn test
```

Expected: **12 tests passing**.

## Hint

Sort with a composed `Comparator` (score DESC, time ASC, date ASC). Walk the sorted list and assign ranks: track position (1-indexed) and the previous score — when the score matches the previous entry, assign the same rank; otherwise assign the current position.
