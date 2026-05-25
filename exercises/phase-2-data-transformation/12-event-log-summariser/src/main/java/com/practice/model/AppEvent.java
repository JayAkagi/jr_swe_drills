package com.practice.model;

import java.time.LocalDateTime;

public class AppEvent {
    private final String eventId;
    private final LocalDateTime timestamp;
    private final EventType eventType;
    private final String userId;
    private final long durationMs;
    private final boolean success;

    public AppEvent(String eventId, LocalDateTime timestamp, EventType eventType,
                    String userId, long durationMs, boolean success) {
        this.eventId = eventId;
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.userId = userId;
        this.durationMs = durationMs;
        this.success = success;
    }

    public String getEventId() { return eventId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public EventType getEventType() { return eventType; }
    public String getUserId() { return userId; }
    public long getDurationMs() { return durationMs; }
    public boolean isSuccess() { return success; }
}
