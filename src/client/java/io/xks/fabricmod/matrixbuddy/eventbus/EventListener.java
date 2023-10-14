package io.xks.fabricmod.matrixbuddy.eventbus;

import io.xks.fabricmod.matrixbuddy.eventbus.events.Event;

public interface EventListener {
    public void onEvent(Event event);
}
