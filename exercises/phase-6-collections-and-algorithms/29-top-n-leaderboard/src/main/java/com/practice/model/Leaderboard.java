package com.practice.model;

import java.time.LocalDateTime;
import java.util.List;

public class Leaderboard {
    private final String gameName;
    private final LocalDateTime generatedAt;
    private final List<LeaderboardEntry> entries;

    public Leaderboard(String gameName, LocalDateTime generatedAt, List<LeaderboardEntry> entries) {
        this.gameName = gameName;
        this.generatedAt = generatedAt;
        this.entries = entries;
    }

    public String getGameName() { return gameName; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public List<LeaderboardEntry> getEntries() { return entries; }
}
