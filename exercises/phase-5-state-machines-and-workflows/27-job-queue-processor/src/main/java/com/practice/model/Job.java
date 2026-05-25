package com.practice.model;

import java.time.LocalDateTime;

public class Job {

    private final String jobId;
    private final String type;
    private final int priority;
    private final String payload;
    private JobStatus status;
    private int attemptCount;
    private final int maxAttempts;
    private final LocalDateTime createdAt;
    private LocalDateTime lastAttemptAt;
    private String errorMessage;

    public Job(String jobId, String type, int priority, String payload, int maxAttempts, LocalDateTime createdAt) {
        this.jobId = jobId;
        this.type = type;
        this.priority = priority;
        this.payload = payload;
        this.maxAttempts = maxAttempts;
        this.createdAt = createdAt;
        this.status = JobStatus.QUEUED;
        this.attemptCount = 0;
        this.lastAttemptAt = null;
        this.errorMessage = null;
    }

    public String getJobId() {
        return jobId;
    }

    public String getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    public String getPayload() {
        return payload;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastAttemptAt() {
        return lastAttemptAt;
    }

    public void setLastAttemptAt(LocalDateTime lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
