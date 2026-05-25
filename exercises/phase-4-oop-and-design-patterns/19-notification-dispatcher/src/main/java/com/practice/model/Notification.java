package com.practice.model;

public class Notification {

    private final String recipientId;
    private final String subject;
    private final String body;
    private final String priority;

    public Notification(String recipientId, String subject, String body, String priority) {
        this.recipientId = recipientId;
        this.subject = subject;
        this.body = body;
        this.priority = priority;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getPriority() {
        return priority;
    }
}
