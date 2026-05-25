package com.practice.service;

import com.practice.model.Event;

public class EventBus {

    public <T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void publish(Event event) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
