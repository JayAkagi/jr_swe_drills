# Exercise 24 — Order Lifecycle State Machine

## Your Task

Implement `InvalidTransitionException` and `OrderStateMachine` in `src/main/java/com/practice/service/`.

## Scenario

An e-commerce platform tracks orders through a strict lifecycle. Orders move from PENDING through to DELIVERED or CANCELLED. Returns are only allowed after delivery. Your state machine must enforce every valid and invalid transition.

## Models

| Class | Fields | Notes |
|-------|--------|-------|
| `OrderStatus` | PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, RETURNED | Pre-built enum |
| `StatusChange` | from, to (OrderStatus), changedAt (LocalDateTime), reason (String) | Pre-built, immutable |
| `Order` | orderId (String), status (OrderStatus), history (List\<StatusChange\>) | Pre-built; status and history are mutable |

## Valid Transitions

| From | To (allowed) |
|------|-------------|
| PENDING | CONFIRMED, CANCELLED |
| CONFIRMED | PROCESSING, CANCELLED |
| PROCESSING | SHIPPED |
| SHIPPED | DELIVERED |
| DELIVERED | RETURNED |
| CANCELLED | (none) |
| RETURNED | (none) |

## Class Descriptions

### `InvalidTransitionException extends RuntimeException`
Thrown when a requested transition is not in the valid transitions table. The message must contain both the source and destination state names.

### `OrderStateMachine`
Single public method: `void transition(Order order, OrderStatus newStatus, String reason)`.
- If the transition is valid: update `order.status` to `newStatus` and append a `StatusChange` (with current time, reason) to `order.history`.
- If the transition is invalid: throw `InvalidTransitionException` with a message naming both states.

## Acceptance Criteria

1. Valid transitions update `order.status` and record an entry in `order.history`.
2. Invalid transitions throw `InvalidTransitionException` whose message contains both state names.
3. Full history is maintained across multiple transitions.
4. DELIVERED → CANCELLED is rejected (use RETURNED instead).

## Test Count

Run `mvn test` — all 13 tests must pass.

## Hint

Use a `Map<OrderStatus, Set<OrderStatus>>` built in a static initialiser to look up allowed targets in O(1).
