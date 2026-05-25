# Exercise 13 — Shopping Cart Discount Engine

## Scenario

You are building the checkout engine for an online retailer. The engine calculates the final price of a shopping cart after applying VAT rules and discount vouchers. Products in different categories have different VAT treatment, and multiple voucher types can be stacked.

## Models

| Class | Key Fields |
|-------|-----------|
| `ProductCategory` | enum: FOOD, ELECTRONICS, CLOTHING, BOOKS |
| `VoucherType` | enum: PERCENTAGE_OFF, FIXED_AMOUNT_OFF, BUY_ONE_GET_ONE |
| `CartItem` | productId, name, category, unitPriceGBP, quantity |
| `Voucher` | code, type, value |
| `Cart` | items (List\<CartItem\>), appliedVouchers (List\<Voucher\>) |
| `DiscountLine` | description, amountGBP |
| `CartTotal` | subtotalGBP, discountBreakdown, totalDiscountGBP, finalTotalGBP |

## Method Signature

```java
public CartTotal calculate(Cart cart)
```

## Criteria

1. ELECTRONICS and CLOTHING items have 20% VAT added to their unit price before any other calculations.
2. FOOD and BOOKS items have no VAT applied.
3. `subtotalGBP` is the sum of (effectiveUnitPrice × quantity) for all items after VAT.
4. BUY_ONE_GET_ONE vouchers give `floor(quantity / 2)` free units per cart item — deduct `floor(q/2) × effectiveUnitPrice` per item.
5. BOGO discounts are applied first, before percentage discounts.
6. PERCENTAGE_OFF vouchers discount = `subtotalGBP × value / 100`, applied to the subtotal after BOGO deductions.
7. FIXED_AMOUNT_OFF vouchers apply a flat deduction after percentage discounts.
8. Each applied discount creates a `DiscountLine` entry in `discountBreakdown`.
9. `totalDiscountGBP` equals the sum of all `DiscountLine.amountGBP` values.
10. `finalTotalGBP = max(0, subtotalGBP - totalDiscountGBP)` — never goes negative.
11. All BigDecimal arithmetic uses `setScale(2, RoundingMode.HALF_UP)`.

## Running Tests

```bash
cd 13-shopping-cart-discount-engine
mvn test
```

Tests will fail with `UnsupportedOperationException` until you implement `CartCalculator.calculate()`.

<details>
<summary>Hint</summary>

Work in phases: first compute `effectiveUnitPrice` per item (apply VAT where needed), then compute `subtotalGBP`. Then iterate over vouchers in order (BOGO first, then PERCENTAGE_OFF, then FIXED_AMOUNT_OFF), building up `discountBreakdown` as you go. Finally sum the breakdown for `totalDiscountGBP` and clamp `finalTotalGBP` to zero.

</details>
