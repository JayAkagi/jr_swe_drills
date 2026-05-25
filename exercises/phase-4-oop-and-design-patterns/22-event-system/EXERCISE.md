# Exercise 22 — Event System (Observer Pattern)

## Your Task

Implement the following classes in `src/main/java/com/practice/service/`:

- `EventListener<T>` (interface — already provided as skeleton)
- `EventBus` — allows subscribing and publishing typed events

## Scenario

An e-commerce platform fires domain events when things happen (order placed, payment received). Different parts of the system (email service, analytics, inventory) need to react to relevant events without being tightly coupled. The `EventBus` decouples publishers from subscribers.

## Models

| Class | Fields |
|-------|--------|
| `Event` (abstract) | eventType (String), occurredAt (LocalDateTime) |
| `OrderPlacedEvent extends Event` | orderId (String), customerId (String), totalGBP (BigDecimal). eventType = "ORDER_PLACED" |
| `PaymentReceivedEvent extends Event` | orderId (String), amountGBP (BigDecimal), paymentMethod (String). eventType = "PAYMENT_RECEIVED" |

## Classes to Implement

### `EventListener<T extends Event>` (interface)
Single method: `void onEvent(T event)`

### `EventBus`
- `<T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener)` — registers a listener for a specific event type
- `void publish(Event event)` — delivers the event to all listeners registered for its runtime type

Rules:
- A listener subscribed to `OrderPlacedEvent.class` only receives `OrderPlacedEvent` instances
- Multiple listeners can subscribe to the same event type — all are called
- If no listeners are registered for the published event type, do nothing (no exception)
- If a listener throws during `onEvent()`, catch the exception and continue notifying remaining listeners

## Criteria

1. Listeners only receive events of the type they subscribed to
2. Multiple listeners for the same type all get called
3. Publishing with no subscribers does nothing (no exception)
4. A throwing listener does not prevent other listeners from receiving the event

## Test Count

Run `mvn test` — expect **12 tests** to pass after full implementation.

<details>
<summary>Hint</summary>

Store listeners in a `Map<Class<?>, List<EventListener<?>>>`. On `subscribe`, add to the list for that class. On `publish`, look up the list by `event.getClass()` and call each listener:

```java
private final Map<Class<?>, List<EventListener<?>>> listeners = new HashMap<>();

public <T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener) {
    listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
}

@SuppressWarnings("unchecked")
public void publish(Event event) {
    List<EventListener<?>> eventListeners = listeners.getOrDefault(event.getClass(), List.of());
    for (EventListener<?> listener : eventListeners) {
        try {
            ((EventListener<Event>) listener).onEvent(event);
        } catch (Exception e) {
            // swallow — other listeners must still be notified
        }
    }
}
```

</details>
