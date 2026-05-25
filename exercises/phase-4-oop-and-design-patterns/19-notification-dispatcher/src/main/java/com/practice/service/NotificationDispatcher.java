package com.practice.service;

import com.practice.model.DeliveryResult;
import com.practice.model.Notification;
import com.practice.model.NotificationChannel;

import java.util.List;
import java.util.Map;

public class NotificationDispatcher {

    private final Map<NotificationChannel, NotificationSender> senders;

    public NotificationDispatcher(Map<NotificationChannel, NotificationSender> senders) {
        this.senders = senders;
    }

    public List<DeliveryResult> dispatch(Notification notification, List<NotificationChannel> channels) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
