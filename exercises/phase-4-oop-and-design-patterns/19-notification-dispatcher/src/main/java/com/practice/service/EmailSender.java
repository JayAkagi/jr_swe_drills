package com.practice.service;

import com.practice.model.DeliveryResult;
import com.practice.model.Notification;
import com.practice.model.NotificationChannel;

public class EmailSender implements NotificationSender {

    @Override
    public DeliveryResult send(Notification notification) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
