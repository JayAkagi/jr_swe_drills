package com.practice.model;

import java.time.LocalDate;

public class ScoreEntry {
    private final String playerId;
    private final String playerName;
    private final int score;
    private final int completionTimeSeconds;
    private final LocalDate achievementDate;

    public ScoreEntry(String playerId, String playerName, int score, int completionTimeSeconds, LocalDate achievementDate) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
        this.completionTimeSeconds = completionTimeSeconds;
        this.achievementDate = achievementDate;
    }

    public String getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public int getCompletionTimeSeconds() { return completionTimeSeconds; }
    public LocalDate getAchievementDate() { return achievementDate; }
}
