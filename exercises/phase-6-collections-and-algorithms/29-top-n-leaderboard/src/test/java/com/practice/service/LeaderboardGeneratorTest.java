package com.practice.service;

import com.practice.model.Leaderboard;
import com.practice.model.LeaderboardEntry;
import com.practice.model.ScoreEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardGeneratorTest {

    private LeaderboardGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new LeaderboardGenerator();
    }

    @Test
    void shouldAssignRankOne_whenSingleEntry() {
        List<ScoreEntry> entries = List.of(
            new ScoreEntry("p1", "Alice", 100, 60, LocalDate.of(2024, 1, 1))
        );
        Leaderboard lb = generator.generate("TestGame", entries, 5);
        assertEquals(1, lb.getEntries().size());
        assertEquals(1, lb.getEntries().get(0).getRank());
    }

    @Test
    void shouldRankHigherScoreFirst() {
        List<ScoreEntry> entries = List.of(
            new ScoreEntry("p1", "Alice", 80, 60, LocalDate.of(2024, 1, 1)),
            new ScoreEntry("p2", "Bob", 100, 60, LocalDate.of(2024, 1, 1))
        );
        Leaderboard lb = generator.generate("TestGame", entries, 5);
        assertEquals("p2", lb.getEntries().get(0).getPlayerId());
        assertEquals(1, lb.getEntries().get(0).getRank());
    }

    @Test
    void shouldBreakTieByFasterCompletionTime() {
        List<ScoreEntry> entries = List.of(
            new ScoreEntry("p1", "Alice", 100, 90, LocalDate.of(2024, 1, 1)),
            new ScoreEntry("p2", "Bob", 100, 60, LocalDate.of(2024, 1, 1))
        );
        Leaderboard lb = generator.generate("TestGame", entries, 5);
        assertEquals("p2", lb.getEntries().get(0).getPlayerId());
    }

    @Test
    void shouldBreakTieByEarlierAchievementDate() {
        List<ScoreEntry> entries = List.of(
            new ScoreEntry("p1", "Alice", 100, 60, LocalDate.of(2024, 2, 1)),
            new ScoreEntry("p2", "Bob", 100, 60, LocalDate.of(2024, 1, 1))
        );
        Leaderboard lb = generator.generate("TestGame", entries, 5);
        assertEquals("p2", lb.getEntries().get(0).getPlayerId());
    }

    @Test
    void shouldShareRankAndSkipNextRank_whenTiedOnScore() {
        List<ScoreEntry> entries = List.of(
            new ScoreEntry("p1", "Alice", 100, 60, LocalDate.of(2024, 1, 1)),
            new ScoreEntry("p2", "Bob", 100, 70, LocalDate.of(2024, 1, 1)),
            new ScoreEntry("p3", "Carol", 80, 60, LocalDate.of(2024, 1, 1))
        );
        Leaderboard lb = generator.generate("TestGame", entries, 5);
        List<LeaderboardEntry> e = lb.getEntries();
        assertEquals(1, e.get(0).getRank());
        assertEquals(1, e.get(1).getRank());
        assertEquals(3, e.get(2).getRank());
    }

    @Test
    void shouldLimitResultsToTopN() {
        List<ScoreEntry> entries = List.of(
            new ScoreEntry("p1", "Alice", 100, 60, LocalDate.of(2024, 1, 1)),
            new ScoreEntry("p2", "Bob", 90, 60, LocalDate.of(2024, 1, 1)),
            new ScoreEntry("p3", "Carol", 80, 60, LocalDate.of(2024, 1, 1)),
            new ScoreEntry("p4", "Dave", 70, 60, LocalDate.of(2024, 1, 1))
        );
        Leaderboard lb = generator.generate("TestGame", entries, 2);
        assertEquals(2, lb.getEntries().size());
    }

    @Test
    void shouldReturnEmptyLeaderboard_whenNoEntries() {
        Leaderboard lb = generator.generate("TestGame", List.of(), 5);
        assertNotNull(lb);
        assertTrue(lb.getEntries().isEmpty());
    }

    @Test
    void shouldReturnAllEntries_whenNLargerThanEntryCount() {
        List<ScoreEntry> entries = List.of(
            new ScoreEntry("p1", "Alice", 100, 60, LocalDate.of(2024, 1, 1)),
            new ScoreEntry("p2", "Bob", 90, 60, LocalDate.of(2024, 1, 1))
        );
        Leaderboard lb = generator.generate("TestGame", entries, 10);
        assertEquals(2, lb.getEntries().size());
    }

    @Test
    void shouldSetGameName() {
        Leaderboard lb = generator.generate("SpeedRun2024", List.of(), 5);
        assertEquals("SpeedRun2024", lb.getGameName());
    }

    @Test
    void shouldSetGeneratedAt_notNull() {
        Leaderboard lb = generator.generate("Game", List.of(), 5);
        assertNotNull(lb.getGeneratedAt());
        assertTrue(lb.getGeneratedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void shouldOrderEntriesCorrectly_highestScoreFirst() {
        List<ScoreEntry> entries = List.of(
            new ScoreEntry("p3", "Carol", 70, 60, LocalDate.of(2024, 1, 1)),
            new ScoreEntry("p1", "Alice", 100, 60, LocalDate.of(2024, 1, 1)),
            new ScoreEntry("p2", "Bob", 85, 60, LocalDate.of(2024, 1, 1))
        );
        Leaderboard lb = generator.generate("TestGame", entries, 5);
        assertEquals("p1", lb.getEntries().get(0).getPlayerId());
        assertEquals("p2", lb.getEntries().get(1).getPlayerId());
        assertEquals("p3", lb.getEntries().get(2).getPlayerId());
    }

    @Test
    void shouldAssignCorrectRankNumbersWithGapAfterTie() {
        List<ScoreEntry> entries = List.of(
            new ScoreEntry("p1", "Alice", 100, 60, LocalDate.of(2024, 1, 1)),
            new ScoreEntry("p2", "Bob", 100, 60, LocalDate.of(2024, 1, 2)),
            new ScoreEntry("p3", "Carol", 90, 60, LocalDate.of(2024, 1, 1)),
            new ScoreEntry("p4", "Dave", 80, 60, LocalDate.of(2024, 1, 1))
        );
        Leaderboard lb = generator.generate("TestGame", entries, 5);
        List<LeaderboardEntry> e = lb.getEntries();
        assertEquals(1, e.get(0).getRank());
        assertEquals(1, e.get(1).getRank());
        assertEquals(3, e.get(2).getRank());
        assertEquals(4, e.get(3).getRank());
    }
}
