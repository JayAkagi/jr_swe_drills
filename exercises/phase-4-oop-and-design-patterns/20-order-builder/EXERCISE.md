# Exercise 20 — Order Builder (Builder Pattern)

## Your Task

Implement `Order` and `Order.Builder` in `src/main/java/com/practice/service/Order.java`

## Scenario

An e-commerce platform needs to construct orders from multiple optional and mandatory fields. Some fields are required for a valid order; others are optional. Rather than a constructor with many parameters, use the Builder pattern to construct `Order` objects in a readable, validated way.

## Models

| Class | Fields |
|-------|--------|
| `OrderItem` | productId (String), quantity (int), unitPriceGBP (BigDecimal) |

## Classes to Implement

### `Order`
An immutable class constructed exclusively via its nested `Builder`. Has these fields:

| Field | Required? |
|-------|-----------|
| orderId | Optional |
| customerId | Yes |
| items | Yes (non-empty) |
| deliveryAddress | Yes |
| billingAddress | Optional |
| paymentMethod | Yes |
| discountCode | Optional |
| priority | Optional |
| notes | Optional |

No setters — only getters.

### `Order.Builder`
Nested static class with fluent setters (each returns `this`) and a `build()` method.

`build()` must validate **all** fields and collect **all** errors before throwing a single `IllegalStateException`. The exception message must include:
- `"no items"` if items list is null or empty
- `"customer"` if customerId is null or blank
- `"delivery address"` if deliveryAddress is null or blank
- `"payment method"` if paymentMethod is null or blank
- `"quantity"` if any item has quantity ≤ 0
- `"price"` if any item has unitPriceGBP ≤ 0

If there are no errors, `build()` returns a new `Order`.

## Criteria

1. Valid orders build without exception
2. All mandatory field violations are reported in one `IllegalStateException`
3. Optional fields may be null
4. The built `Order` is immutable (no setters)
5. Builder is fluent — every setter returns `this`

## Test Count

Run `mvn test` — expect **13 tests** to pass after full implementation.

<details>
<summary>Hint</summary>

Collect errors into a `List<String>`, then if the list is non-empty, join them and throw:

```java
List<String> errors = new ArrayList<>();
if (items == null || items.isEmpty()) errors.add("no items");
if (customerId == null || customerId.isBlank()) errors.add("customer is required");
if (deliveryAddress == null || deliveryAddress.isBlank()) errors.add("delivery address is required");
if (paymentMethod == null || paymentMethod.isBlank()) errors.add("payment method is required");
if (items != null) {
    for (OrderItem item : items) {
        if (item.getQuantity() <= 0) errors.add("quantity must be positive");
        if (item.getUnitPriceGBP().compareTo(BigDecimal.ZERO) <= 0) errors.add("price must be positive");
    }
}
if (!errors.isEmpty()) throw new IllegalStateException(String.join("; ", errors));
return new Order(this);
```

</details>
