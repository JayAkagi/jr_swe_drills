package com.practice.model;

public class DeliveryResult {

    private final String recipientId;
    private final NotificationChannel channel;
    private final boolean success;
    private final String errorMessage;

    public DeliveryResult(String recipientId, NotificationChannel channel, boolean success, String errorMessage) {
        this.recipientId = recipientId;
        this.channel = channel;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
