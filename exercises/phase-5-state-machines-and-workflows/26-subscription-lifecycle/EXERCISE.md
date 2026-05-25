# Exercise 26 — Subscription Lifecycle Manager

## Your Task

Implement `SubscriptionManager` in `src/main/java/com/practice/service/`.

## Scenario

A SaaS product manages subscriber accounts through a lifecycle: trial, active, past-due (when payment fails), suspended (after prolonged non-payment), and cancelled. Your manager enforces the rules for each transition, records every event, and updates date fields correctly.

## Models

| Class | Fields | Notes |
|-------|--------|-------|
| `SubscriptionStatus` | TRIAL, ACTIVE, PAST_DUE, SUSPENDED, CANCELLED | Pre-built enum |
| `SubscriptionEvent` | event (String), occurredAt (LocalDate), note (String) | Pre-built, immutable |
| `Subscription` | subscriptionId, customerId, plan, status, startDate, trialEndDate, renewalDate, cancellationDate, events | Pre-built; status=TRIAL initially; renewalDate and cancellationDate start null |

## Transition Rules

| Method | Allowed From | Condition | Result |
|--------|-------------|-----------|--------|
| `activate` | TRIAL | today >= trialEndDate | ACTIVE; renewalDate = today + 30 days |
| `activate` | TRIAL | today < trialEndDate | throw IllegalStateException |
| `activate` | non-TRIAL | — | throw IllegalStateException |
| `renewalFailed` | ACTIVE | — | PAST_DUE |
| `renewalFailed` | non-ACTIVE | — | throw IllegalStateException |
| `suspend` | PAST_DUE | today > renewalDate + 7 days | SUSPENDED |
| `suspend` | PAST_DUE | within 7-day grace | throw IllegalStateException |
| `suspend` | non-PAST_DUE | — | throw IllegalStateException |
| `cancel` | any non-CANCELLED | — | CANCELLED; cancellationDate = today |
| `cancel` | CANCELLED | — | throw IllegalStateException |
| `reactivate` | PAST_DUE or SUSPENDED | — | ACTIVE; renewalDate = today + 30 days |
| `reactivate` | other | — | throw IllegalStateException |

## Class Descriptions

### `SubscriptionManager`
Five public methods, each taking `(Subscription s, LocalDate today)`:
- `void activate(Subscription s, LocalDate today)`
- `void renewalFailed(Subscription s, LocalDate today)`
- `void suspend(Subscription s, LocalDate today)`
- `void cancel(Subscription s, LocalDate today)`
- `void reactivate(Subscription s, LocalDate today)`

Each method updates `s.status`, relevant date fields, and appends a `SubscriptionEvent` to `s.events`.

## Acceptance Criteria

1. All transitions are validated; invalid attempts throw `IllegalStateException`.
2. Every state change is recorded in the event history with a descriptive note.
3. `cancellationDate` is set when a subscription is cancelled.
4. A trial subscription cannot be activated before `trialEndDate`.
5. `renewalDate` is set to today + 30 days on `activate` and `reactivate`.
6. `suspend` is only allowed more than 7 days after `renewalDate`.

## Test Count

Run `mvn test` — all 14 tests must pass.

## Hint

For the `suspend` grace-period check, use `today.isAfter(s.getRenewalDate().plusDays(7))`.
