package com.practice.model;

public class UserSummary {
    private final String userId;
    private final int totalEvents;
    private final double successRate;
    private final double avgDurationMs;
    private final EventType mostFrequentEventType;

    public UserSummary(String userId, int totalEvents, double successRate,
                       double avgDurationMs, EventType mostFrequentEventType) {
        this.userId = userId;
        this.totalEvents = totalEvents;
        this.successRate = successRate;
        this.avgDurationMs = avgDurationMs;
        this.mostFrequentEventType = mostFrequentEventType;
    }

    public String getUserId() { return userId; }
    public int getTotalEvents() { return totalEvents; }
    public double getSuccessRate() { return successRate; }
    public double getAvgDurationMs() { return avgDurationMs; }
    public EventType getMostFrequentEventType() { return mostFrequentEventType; }
}
