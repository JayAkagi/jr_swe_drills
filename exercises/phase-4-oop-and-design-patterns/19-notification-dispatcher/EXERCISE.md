# Exercise 19 — Notification Dispatcher (Strategy Pattern)

## Your Task

Implement the following classes in `src/main/java/com/practice/service/`:

- `NotificationSender` (interface — already provided as skeleton)
- `EmailSender` — sends via email, returns success
- `SmsSender` — sends via SMS, returns success
- `PushSender` — sends via push notification, returns success
- `NotificationDispatcher` — routes notifications to the correct sender per channel

## Scenario

A notification service needs to send messages through multiple channels (email, SMS, push) without the dispatcher knowing the details of each delivery mechanism. Each channel has its own sender implementation, and the dispatcher should be able to use any combination of channels without modification.

## Models

| Class | Fields |
|-------|--------|
| `NotificationChannel` | enum: EMAIL, SMS, PUSH |
| `Notification` | recipientId (String), subject (String), body (String), priority (String) |
| `DeliveryResult` | recipientId (String), channel (NotificationChannel), success (boolean), errorMessage (String) |

## Classes to Implement

### `NotificationSender` (interface)
Single method: `DeliveryResult send(Notification notification)`

### `EmailSender implements NotificationSender`
Sends via email. Returns a `DeliveryResult` with `success=true` and `errorMessage=null`.

### `SmsSender implements NotificationSender`
Sends via SMS. Returns a `DeliveryResult` with `success=true` and `errorMessage=null`.

### `PushSender implements NotificationSender`
Sends via push. Returns a `DeliveryResult` with `success=true` and `errorMessage=null`.

### `NotificationDispatcher`
- Constructor: `NotificationDispatcher(Map<NotificationChannel, NotificationSender> senders)`
- Method: `List<DeliveryResult> dispatch(Notification notification, List<NotificationChannel> channels)`
- Iterates through the requested channels, calls the corresponding sender, collects results.
- If a sender throws an exception, that channel's result should have `success=false` and `errorMessage` set to the exception message — other channels must still be attempted.

## Criteria

1. `dispatch()` delegates to the correct sender for each channel
2. A failed send on one channel does not prevent other channels from being attempted
3. Results contain exactly one entry per channel attempted
4. Adding a new channel requires zero changes to `NotificationDispatcher`

## Test Count

Run `mvn test` — expect **12 tests** to pass after full implementation.

<details>
<summary>Hint</summary>

The dispatcher iterates through the channels list. For each channel it looks up the sender from the map, calls `sender.send(notification)`, and adds the result to the list. Wrap the `send()` call in a try/catch so that if it throws a `RuntimeException`, you create a `DeliveryResult` with `success=false` and `errorMessage=exception.getMessage()`.

```java
for (NotificationChannel channel : channels) {
    try {
        NotificationSender sender = senders.get(channel);
        results.add(sender.send(notification));
    } catch (RuntimeException e) {
        results.add(new DeliveryResult(notification.getRecipientId(), channel, false, e.getMessage()));
    }
}
```

</details>
