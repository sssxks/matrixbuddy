package io.xks.fabricmod.matrixbuddy.eventbus;

import io.xks.fabricmod.matrixbuddy.eventbus.events.Event;

public interface EventListener {
    void onEvent(Event event);
}
