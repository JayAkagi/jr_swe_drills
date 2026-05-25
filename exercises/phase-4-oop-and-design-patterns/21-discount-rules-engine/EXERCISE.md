# Exercise 21 — Discount Rules Engine (Chain of Responsibility)

## Your Task

Implement the following classes in `src/main/java/com/practice/service/`:

- `DiscountRule` — abstract base class for a rule in the chain
- `PlatinumTierDiscount` — 30% for PLATINUM customers
- `LoyaltyHighValueDiscount` — 20% for loyalty members spending strictly over £200
- `BulkOrderDiscount` — 15% for orders with 10 or more items
- `StandardDiscount` — 5% fallback for all orders
- `DiscountEngine` — assembles the chain and provides `calculate()`

## Scenario

A retail platform needs to apply exactly one discount per order, determined by a priority chain of rules. The chain is evaluated top-to-bottom; the first matching rule wins. Adding a new rule should not require changes to existing rule classes.

## Models

| Class | Fields |
|-------|--------|
| `CustomerTier` | enum: BRONZE, SILVER, GOLD, PLATINUM |
| `OrderContext` | customerId (String), orderValueGBP (BigDecimal), isLoyaltyMember (boolean), itemCount (int), customerTier (CustomerTier) |
| `DiscountResult` | discountPercent (int), reason (String) |

## Classes to Implement

### `DiscountRule` (abstract)
- Abstract method: `DiscountResult apply(OrderContext ctx)`
- Field: `DiscountRule next` with getter/setter
- Helper: `protected DiscountResult passToNext(OrderContext ctx)` — calls `next.apply(ctx)` if next is set, otherwise returns `null`

### `PlatinumTierDiscount extends DiscountRule`
Matches when `customerTier == PLATINUM`. Returns `DiscountResult(30, "Platinum tier discount")`. Otherwise calls `passToNext(ctx)`.

### `LoyaltyHighValueDiscount extends DiscountRule`
Matches when `isLoyaltyMember == true` AND `orderValueGBP > 200` (strictly greater). Returns `DiscountResult(20, "Loyalty high-value discount")`. Otherwise calls `passToNext(ctx)`.

### `BulkOrderDiscount extends DiscountRule`
Matches when `itemCount >= 10`. Returns `DiscountResult(15, "Bulk order discount")`. Otherwise calls `passToNext(ctx)`.

### `StandardDiscount extends DiscountRule`
Fallback — always matches. Returns `DiscountResult(5, "Standard discount")`. Does not call next.

### `DiscountEngine`
Constructor builds the chain: `PlatinumTierDiscount → LoyaltyHighValueDiscount → BulkOrderDiscount → StandardDiscount`.
Method `DiscountResult calculate(OrderContext ctx)` starts the chain by calling `chain.apply(ctx)`.

## Criteria

1. Only one discount applies (first matching rule wins)
2. Chain order: Platinum → LoyaltyHighValue → Bulk → Standard
3. Every order gets at least `StandardDiscount` (5%)
4. Adding a new rule requires no changes to existing rule classes

## Test Count

Run `mvn test` — expect **12 tests** to pass after full implementation.

<details>
<summary>Hint</summary>

Link the chain in `DiscountEngine`'s constructor:

```java
PlatinumTierDiscount platinum = new PlatinumTierDiscount();
LoyaltyHighValueDiscount loyalty = new LoyaltyHighValueDiscount();
BulkOrderDiscount bulk = new BulkOrderDiscount();
StandardDiscount standard = new StandardDiscount();

platinum.setNext(loyalty);
loyalty.setNext(bulk);
bulk.setNext(standard);

this.chain = platinum;
```

Each rule's `apply()` follows this pattern:
```java
if (/* condition matches */) {
    return new DiscountResult(percent, "reason");
}
return passToNext(ctx);
```

And `passToNext`:
```java
protected DiscountResult passToNext(OrderContext ctx) {
    if (next != null) return next.apply(ctx);
    return null;
}
```

</details>
