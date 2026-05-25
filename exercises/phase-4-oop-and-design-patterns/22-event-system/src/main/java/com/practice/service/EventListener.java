package com.practice.service;

import com.practice.model.Event;

public interface EventListener<T extends Event> {

    void onEvent(T event);
}
