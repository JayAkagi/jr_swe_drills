package com.practice.model;

import java.time.LocalDateTime;
import java.util.List;

public class EventSummary {
    private final LocalDateTime periodStart;
    private final LocalDateTime periodEnd;
    private final int totalEvents;
    private final double errorRate;
    private final List<UserSummary> topUsersByActivity;
    private final List<String> errorSpikes;

    public EventSummary(LocalDateTime periodStart, LocalDateTime periodEnd, int totalEvents,
                        double errorRate, List<UserSummary> topUsersByActivity, List<String> errorSpikes) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.totalEvents = totalEvents;
        this.errorRate = errorRate;
        this.topUsersByActivity = topUsersByActivity;
        this.errorSpikes = errorSpikes;
    }

    public LocalDateTime getPeriodStart() { return periodStart; }
    public LocalDateTime getPeriodEnd() { return periodEnd; }
    public int getTotalEvents() { return totalEvents; }
    public double getErrorRate() { return errorRate; }
    public List<UserSummary> getTopUsersByActivity() { return topUsersByActivity; }
    public List<String> getErrorSpikes() { return errorSpikes; }
}
