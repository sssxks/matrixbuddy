package io.xks.fabricmod.matrixbuddy.eventbus;

import io.xks.fabricmod.matrixbuddy.eventbus.events.Event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private static final ConcurrentHashMap<Class<? extends Event>, CopyOnWriteArrayList<EventListener>> listeners = new ConcurrentHashMap<>();

    public static void subscribe(Class<? extends Event> eventType, EventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public static void unsubscribe(Class<? extends Event> eventType, EventListener listener) {
        CopyOnWriteArrayList<EventListener> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    public static void publish(Event event) {
        CopyOnWriteArrayList<EventListener> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (EventListener listener : eventListeners) {
                listener.onEvent(event);
            }
        }
    }

}
