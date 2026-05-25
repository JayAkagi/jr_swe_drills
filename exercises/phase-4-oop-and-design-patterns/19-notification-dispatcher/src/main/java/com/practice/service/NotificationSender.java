package com.practice.service;

import com.practice.model.DeliveryResult;
import com.practice.model.Notification;

public interface NotificationSender {

    DeliveryResult send(Notification notification);
}
