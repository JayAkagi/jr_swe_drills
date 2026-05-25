package com.practice.model;

public class LeaderboardEntry {
    private final int rank;
    private final String playerId;
    private final String playerName;
    private final int score;
    private final int completionTimeSeconds;

    public LeaderboardEntry(int rank, String playerId, String playerName, int score, int completionTimeSeconds) {
        this.rank = rank;
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
        this.completionTimeSeconds = completionTimeSeconds;
    }

    public int getRank() { return rank; }
    public String getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public int getCompletionTimeSeconds() { return completionTimeSeconds; }
}
